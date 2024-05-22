package org.camunda.bpm.blps.lab4.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class MailingDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("message sent");
        /*String login = (String) delegateExecution.getVariable("login");
        String password = (String) delegateExecution.getVariable("password");
        boolean isAdmin = login.equals("admin") && password.equals("admin");
        delegateExecution.setVariable("is_admin", isAdmin);*/
    }
}
