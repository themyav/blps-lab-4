package org.camunda.bpm.blps.lab4.delegate;

import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.service.UserService;
import org.camunda.bpm.blps.lab4.service.VacancyService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserSearchDelegate implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private VacancyService vacancyService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String login = (String) delegateExecution.getVariable("login");
        String password = (String) delegateExecution.getVariable("password");


        User user = userService.findUserByEmails(login);
        delegateExecution.setVariable("myList", Arrays.toString(vacancyService.getAllByUserId(user.getId()).toArray()));

        if(user == null) {
            delegateExecution.setVariable("user_exists", false);
            delegateExecution.setVariable("is_admin", false);
            return;
        }
        boolean isAdmin = user.getModerator();//login.equals("admin") && password.equals("admin");
        delegateExecution.setVariable("is_admin", isAdmin);
        delegateExecution.setVariable("user_exists", true);

    }
}
