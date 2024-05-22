package org.camunda.bpm.blps.lab4.service;

import org.camunda.bpm.blps.lab4.model.Privilege;
import org.camunda.bpm.blps.lab4.model.Role;
import org.camunda.bpm.blps.lab4.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void create(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

    public Role findByName(String name){
        if(name == null) return null; //TODO check
        return roleRepository.findByName(name);
    }
}
