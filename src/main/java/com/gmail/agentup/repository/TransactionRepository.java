package com.gmail.agentup.repository;

import com.gmail.agentup.model.Account;
import com.gmail.agentup.model.Customer;
import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.userOwner.id = :id")
    List<Transaction> findAllUserTransactions(@Param("id") Long id);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:customerId is null OR t.customer.id = :customerId)" +
            "AND (:accountId is null OR t.account.id = :accountId)" +
            "AND (:entryId is null OR t.entry.id = :entryId)" +
            "AND t.userOwner.id = :id")
    List<Transaction> findUserTransactionsByFilter(@Param("id") Long id,
                                                   @Param("customerId") Long customerId,
                                                   @Param("accountId") Long accountId,
                                                   @Param("entryId") Long entryId);
}
