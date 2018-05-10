package com.gmail.agentup.repository;

import com.gmail.agentup.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.userOwner.id = :id")
    List<Account> findAllUserAccounts(@Param("id") Long id);

}
