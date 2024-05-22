package org.camunda.bpm.blps.lab4.delegate.vacancy;

import camundajar.impl.scala.Int;
import org.camunda.bpm.blps.lab4.model.Vacancy;
import org.camunda.bpm.blps.lab4.repo.VacancyRepository;
import org.camunda.bpm.blps.lab4.service.VacancyService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VacancyGetDelegate implements JavaDelegate {
    @Autowired
    private VacancyService vacancyService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Integer id = (Integer) delegateExecution.getVariable("moderation_form");
        Vacancy vacancy = vacancyService.getById(Long.parseLong(id.toString()));
        if(vacancy == null){
            throw new BpmnError("No_Vacancy", "такой вакансии нет");
        }
        //get vacancy
        delegateExecution.setVariable("current_vacancy_id", vacancy.getId());
        delegateExecution.setVariable("current_vacancy_title", vacancy.getTitle());
        delegateExecution.setVariable("current_vacancy_description", vacancy.getDescription());


    }
}
