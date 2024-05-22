package org.camunda.bpm.blps.lab4.component;

import org.camunda.bpm.blps.lab4.model.Privilege;
import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.model.Vacancy;
import org.camunda.bpm.blps.lab4.service.*;
import org.camunda.bpm.blps.lab4.util.enums.ModeratorPrivileges;
import org.camunda.bpm.blps.lab4.util.enums.UserPrivileges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;
        ArrayList<Privilege>userPrivilege = new ArrayList<>();
        for(UserPrivileges privileges : UserPrivileges.values()){
            userPrivilege.add(privilegeService.create(privileges.name()));
        }

        ArrayList<Privilege>moderatorPrivilege = new ArrayList<>();
        for(ModeratorPrivileges privileges : ModeratorPrivileges.values()){
            moderatorPrivilege.add(privilegeService.create(privileges.name()));
        }
        roleService.create("USER", userPrivilege);

        moderatorPrivilege.addAll(userPrivilege);
        roleService.create("MODERATOR", moderatorPrivilege);

        User moderator = new User("Кристина", "Мявук", "krystyna-myar@mail.ru", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        moderator.setRoles(Collections.singletonList(roleService.findByName("MODERATOR")));
        userService.save(moderator);
        balanceService.deposit(moderator.getId(), 1000.0);

        User user = new User("Иван", "Иванов", "Myavochka1119@gmail.com", "$2a$12$jdwdPUMEqNBrxmjyxJhKBuExnSjjKgGu3.W/PcizEn8c4koXfj5Je");
        user.setRoles(Collections.singletonList(roleService.findByName("USER")));
        userService.save(user);

        balanceService.deposit(user.getId(), 300.0);


        Vacancy dvornik = new Vacancy(user, "Дворник", "Мести пол");
        dvornik.setOnModeration(true);
        vacancyService.sendToModeration(dvornik);

        userService.setModerators();
        userService.setUsers();


        alreadySetup = true;
    }

}
