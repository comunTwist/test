package com.gmail.agentup.services;

import com.gmail.agentup.model.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(Long id);

    List<Account> getAllUserAccounts(Long userId);

    void addAccount(Account account);

    void updateAccount(Account account);

    void deleteAccount(Account account);
}
