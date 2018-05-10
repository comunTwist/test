package com.gmail.agentup.services;

import com.gmail.agentup.model.UserOwner;

import java.util.List;

public interface UserService {
    UserOwner getUserByLogin(String login);

    UserOwner getUserById(Long id);

    List<UserOwner> getAllUsers();

    boolean existsByLogin(String login);

    void addUser(UserOwner userOwner);

    void updateUser(UserOwner userOwner);

    void deleteUser(UserOwner userOwner);
}
