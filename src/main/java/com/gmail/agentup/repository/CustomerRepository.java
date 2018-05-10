package com.gmail.agentup.repository;

import com.gmail.agentup.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.userOwner.id = :id")
    List<Customer> findAllUserCustomers(@Param("id") Long id);
}
