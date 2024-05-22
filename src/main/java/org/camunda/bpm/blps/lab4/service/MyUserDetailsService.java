package org.camunda.bpm.blps.lab4.service;

import org.camunda.bpm.blps.lab4.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException{
        org.camunda.bpm.blps.lab4.model.User user = userRepository.findByEmail(username); //username = email
        if(user == null) throw new UsernameNotFoundException("нет такого пользователя");
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getStringRoles())
                .authorities(user.getAuthorities())//("VIEW_ALL_VACANCY", "POST_VACANCY", "VIEW_ALL_MODERATION")
                .build();
    }
}
