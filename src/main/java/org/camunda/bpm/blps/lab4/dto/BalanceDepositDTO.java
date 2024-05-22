package org.camunda.bpm.blps.lab4.dto;

public class BalanceDepositDTO {
    Long userId;
    Double amount;

    public BalanceDepositDTO(Long userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
