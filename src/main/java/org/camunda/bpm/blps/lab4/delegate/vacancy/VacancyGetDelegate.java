package org.camunda.bpm.blps.lab4.delegate.vacancy;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class VacancyGetDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //get vacancy
        delegateExecution.setVariable("current_vacancy_id", 1);
        delegateExecution.setVariable("current_vacancy_title", "1C MEGA dev");
        delegateExecution.setVariable("current_vacancy_description", "hard 1C working");


    }
}
