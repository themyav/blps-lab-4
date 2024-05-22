package org.camunda.bpm.blps.lab4.service;

import org.camunda.bpm.blps.lab4.model.User;
import org.camunda.bpm.blps.lab4.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private List<User> moderators;

    private List<User>users;


    public User save(User user){
        if (userRepository.existsByEmail(user.getEmail())) return null;
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return users;
    }

    public List<User>getSubscribedUsers(){
        return users == null ? null : users.stream().filter(User::getEmailSubscription).toList();
    }

    public void setUsers(){
        users = userRepository.findAll().stream().toList();
    }

    public List<User> getModerators(){
        return moderators;
    }

    public void setModerators(){
        moderators = userRepository.findAll().stream().filter(User::getModerator).toList();
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByEmails(String email) {return userRepository.findByEmail(email);}


}
