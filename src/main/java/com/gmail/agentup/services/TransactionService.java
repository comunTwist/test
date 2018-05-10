package com.gmail.agentup.services;

import com.gmail.agentup.model.Account;
import com.gmail.agentup.model.Customer;
import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction getTransactionById(Long id);

    List<Transaction> getAllUserTransactions(Long userId);

    List<Transaction> getUserTransactionsByFilter(Long userId, Long customerId, Long accountId, Long entryId);

    void addTransaction(Transaction transaction, Account account);

    void updateTransaction(Transaction transaction);

    void deleteTransaction(Transaction transaction);
}
