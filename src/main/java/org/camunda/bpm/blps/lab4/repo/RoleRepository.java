package org.camunda.bpm.blps.lab4.repo;

import org.camunda.bpm.blps.lab4.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}