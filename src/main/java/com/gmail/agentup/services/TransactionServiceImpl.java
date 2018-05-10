package com.gmail.agentup.services;

import com.gmail.agentup.model.Account;
import com.gmail.agentup.model.Customer;
import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.Transaction;
import com.gmail.agentup.repository.AccountRepository;
import com.gmail.agentup.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private AccountRepository accountRepository;

  @Override
  @Transactional(readOnly = true)
  public Transaction getTransactionById(Long id) {
    return transactionRepository.getOne(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Transaction> getAllUserTransactions(Long userId) {
    return transactionRepository.findAllUserTransactions(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Transaction> getUserTransactionsByFilter(Long userId, Long customerId, Long accountId, Long entryId) {
    return transactionRepository.findUserTransactionsByFilter(userId, customerId, accountId, entryId);
  }

  @Override
  @Transactional
  public void addTransaction(Transaction transaction, Account account) {
    accountRepository.save(account);
    transactionRepository.save(transaction);
  }

  @Override
  @Transactional
  public void updateTransaction(Transaction transaction) {
    transactionRepository.save(transaction);
  }

  @Override
  @Transactional
  public void deleteTransaction(Transaction transaction) {
    transactionRepository.delete(transaction);
  }
}
