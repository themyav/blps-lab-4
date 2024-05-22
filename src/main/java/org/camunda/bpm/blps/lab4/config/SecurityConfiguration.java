package org.camunda.bpm.blps.lab4.config;

import org.camunda.bpm.blps.lab4.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailsService userDetailService;

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //disable csrf for POST request!
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers( "/h2-console/**").permitAll();
                    registry.requestMatchers( "/camunda/**").permitAll();
            registry.requestMatchers("/vacancy/published").permitAll();
            registry.requestMatchers("/vacancy/draft").hasAuthority("POST_DRAFT");
            registry.requestMatchers("/vacancy/publish").hasAuthority("POST_VACANCY");

            registry.requestMatchers("vacancy/moderation").hasAuthority("VIEW_ALL_MODERATION"); //+
            registry.requestMatchers("vacancy/moderation/*/publish").hasAuthority("PUBLISH_MODERATED"); //+
            registry.requestMatchers("vacancy/moderation/*/decline").hasAuthority("DECLINE_MODERATED"); //+

            registry.requestMatchers("balance/*/deposit").hasAuthority("DEPOSIT_OWN_BALANCE");
            registry.anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults()).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}