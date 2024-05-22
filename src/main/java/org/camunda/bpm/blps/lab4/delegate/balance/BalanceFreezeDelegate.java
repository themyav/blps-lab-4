package org.camunda.bpm.blps.lab4.delegate.balance;

import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.model.Vacancy;
import org.camunda.bpm.blps.lab4.service.UserService;
import org.camunda.bpm.blps.lab4.service.VacancyService;
import org.camunda.bpm.blps.lab4.util.enums.Result;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BalanceFreezeDelegate implements JavaDelegate {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private UserService userService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String title =  (String) delegateExecution.getVariable("vacancy_name");
        String description = (String) delegateExecution.getVariable("vacancy_desciption");
        String login = (String) delegateExecution.getVariable("login");
        User user = userService.findUserByEmails(login);

        Vacancy vacancy = new Vacancy();
        vacancy.setTitle(title);
        vacancy.setDescription(description);
        vacancy.setAuthorId(user.getId());

        Result publishResult = vacancyService.processPublication(vacancy);

        if (publishResult == Result.OK) {
           // return ResponseEntity.ok(publishResult); //TODO maybe published vacancy
        } else {
            String errorMessage = publishResult.getMessage();
            if (publishResult == Result.USER_NOT_FOUND) {
                throw new BpmnError("User_Not_Found", errorMessage);
            } else {
                throw new BpmnError("Incorrect_Vacancy", errorMessage);
            }
        }
        System.out.println("balance is frozen");

    }
}
