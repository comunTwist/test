package com.gmail.agentup.services;

import com.gmail.agentup.model.Account;
import com.gmail.agentup.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllUserAccounts(Long userId) {
        return accountRepository.findAllUserAccounts(userId);
    }

    @Override
    @Transactional
    public void addAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

}
