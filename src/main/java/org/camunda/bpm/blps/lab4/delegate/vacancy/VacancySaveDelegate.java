package org.camunda.bpm.blps.lab4.delegate.vacancy;

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
public class VacancySaveDelegate implements JavaDelegate {

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private UserService userService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("vacancy is saved");
        String title =  (String) delegateExecution.getVariable("vacancy_name");
        String description = (String) delegateExecution.getVariable("vacancy_desciption");
        String login = (String) delegateExecution.getVariable("login");
        User user = userService.findUserByEmails(login);

        Vacancy vacancy = new Vacancy();
        vacancy.setTitle(title);
        vacancy.setDescription(description);
        vacancy.setAuthorId(user.getId());

        Result result = vacancyService.validateVacancy(vacancy);
        if(result.getCode() != 0) {
            delegateExecution.setVariable("message", result.getMessage());
            throw new BpmnError("Incorrect_Vacancy", "вакансия не прошла валидацию");
        }
        Vacancy savedVacancy = vacancyService.saveAsDraft(vacancy);
        delegateExecution.setVariable("saved_id", savedVacancy.getId());
        delegateExecution.setVariable("saved_title", savedVacancy.getTitle());
        delegateExecution.setVariable("saved_description", savedVacancy.getDescription());
    }
}
