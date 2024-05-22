package org.camunda.bpm.blps.lab4.repo;

import org.camunda.bpm.blps.lab4.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
}
