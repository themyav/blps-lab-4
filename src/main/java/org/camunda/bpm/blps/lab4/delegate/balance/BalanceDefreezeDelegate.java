package org.camunda.bpm.blps.lab4.delegate.balance;

import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.model.Vacancy;
import org.camunda.bpm.blps.lab4.service.UserService;
import org.camunda.bpm.blps.lab4.service.VacancyService;
import org.camunda.bpm.blps.lab4.util.ModeratorComment;
import org.camunda.bpm.blps.lab4.util.enums.Result;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@Component
public class BalanceDefreezeDelegate implements JavaDelegate {

    @Autowired
    private UserService userService;

    @Autowired
    private VacancyService vacancyService;

    public ResponseEntity<?> publishModerated(@PathVariable Long id){
        Result result = vacancyService.publishModerated(id);
        if(result == Result.OK) return ResponseEntity.ok().body(result.getMessage());
        else return ResponseEntity.badRequest().body(result.getMessage());
    }

    public ResponseEntity<?> declineModerated(@PathVariable Long id, @RequestBody String comment){
        Result result = vacancyService.declineModerated(id, new ModeratorComment(comment));
        if(result == Result.OK) return ResponseEntity.ok().body(result.getMessage());
        else return ResponseEntity.badRequest().body(result.getMessage());
    }
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String title =  (String) delegateExecution.getVariable("vacancy_name");
        String description = (String) delegateExecution.getVariable("vacancy_desciption");
        String login = (String) delegateExecution.getVariable("login");
        String option = (String) delegateExecution.getVariable("moderator_choice");
        User user = userService.findUserByEmails(login);

        Vacancy vacancy = new Vacancy();
        vacancy.setTitle(title);
        vacancy.setDescription(description);
        vacancy.setAuthorId(user.getId());

        if(Objects.equals(option, "accept")){
            Result result = vacancyService.publishModerated(vacancy.getId());
            //if(result == Result.OK) return ResponseEntity.ok().body(result.getMessage());
            //else return ResponseEntity.badRequest().body(result.getMessage());
        }
        else{
            String comment = (String) delegateExecution.getVariable("moderator_comment");
            Result result = vacancyService.declineModerated(vacancy.getId(), new ModeratorComment(comment));
        }

        System.out.println("balance de-frozen");
    }
}
