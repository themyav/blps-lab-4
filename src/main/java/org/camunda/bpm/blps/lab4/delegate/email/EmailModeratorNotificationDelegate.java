package org.camunda.bpm.blps.lab4.delegate.email;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class EmailModeratorNotificationDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("AAAAAA");
        delegateExecution.setVariable("already_sent", true);
        //for(int i = 0; i < 100; i++) System.out.println("async");
    }
}
