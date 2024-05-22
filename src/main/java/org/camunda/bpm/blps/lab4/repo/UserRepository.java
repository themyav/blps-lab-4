package org.camunda.bpm.blps.lab4.repo;

import org.camunda.bpm.blps.lab4.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}