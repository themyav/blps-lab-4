package org.camunda.bpm.blps.lab4.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirstDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String login = (String) delegateExecution.getVariable("login");
        String password = (String) delegateExecution.getVariable("password");
        boolean isAdmin = login.equals("admin") && password.equals("admin");
        ArrayList<String> testList = new ArrayList<>();
        testList.add("a");
        testList.add("b");
        testList.add("c");
        delegateExecution.setVariable("myList", testList);
        delegateExecution.setVariable("is_admin", isAdmin);
    }
}
