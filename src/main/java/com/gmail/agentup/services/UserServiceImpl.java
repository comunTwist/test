package com.gmail.agentup.services;

import com.gmail.agentup.model.UserOwner;
import com.gmail.agentup.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserOwner getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    @Transactional(readOnly = true)
    public UserOwner getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOwner> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    @Transactional
    public void addUser(UserOwner userOwner) {
        userRepository.save(userOwner);
    }

    @Override
    @Transactional
    public void updateUser(UserOwner userOwner) {
        userRepository.save(userOwner);
    }

    @Override
    @Transactional
    public void deleteUser(UserOwner userOwner) {
        userRepository.delete(userOwner);
    }

}
