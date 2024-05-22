package org.camunda.bpm.blps.lab4.delegate.balance;

import org.camunda.bpm.blps.lab4.model.Balance;
import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.service.BalanceService;
import org.camunda.bpm.blps.lab4.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceGetDelegate implements JavaDelegate {
    @Autowired
    private BalanceService balanceService;

    @Autowired
    private UserService userService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("getting user's balance");
        String login = (String) delegateExecution.getVariable("login");
        User user = userService.findUserByEmails(login);
        Balance balance = balanceService.get(user.getId());
        if(balance.getRealAmount() >= balanceService.PUBLISH_COST){
            delegateExecution.setVariable("balance_enough", true);
        }
        else delegateExecution.setVariable("balance_enough", false);

    }
}
