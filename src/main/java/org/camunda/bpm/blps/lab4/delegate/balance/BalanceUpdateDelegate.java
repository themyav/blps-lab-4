package org.camunda.bpm.blps.lab4.delegate.balance;

import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.service.BalanceService;
import org.camunda.bpm.blps.lab4.service.UserService;
import org.camunda.bpm.blps.lab4.util.enums.Result;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.feel.syntaxtree.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BalanceUpdateDelegate implements JavaDelegate {
    @Autowired
    private BalanceService balanceService;

    @Autowired
    private UserService userService;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Double amount;
        try{
            amount = (Double) delegateExecution.getVariable("number_balance");
        }catch (ClassCastException e){
            Integer intAmount = (Integer) delegateExecution.getVariable("number_balance");
            amount = Double.parseDouble(intAmount.toString());
        }

        String login = (String) delegateExecution.getVariable("login");
        User user = userService.findUserByEmails(login);

        System.out.println("balance is updated");
        if(amount == null) throw new BpmnError("No_Money", "Сумма для внесения не указана");
        if(amount < balanceService.MIN_DEPOSIT) {
            throw new BpmnError("Too_Less", "Необходимо внести не менее " + balanceService.MIN_DEPOSIT);
        }
        Result result = balanceService.deposit(user.getId(), amount);
        String message = result.getMessage();
        delegateExecution.setVariable("message", message);
    }
}
