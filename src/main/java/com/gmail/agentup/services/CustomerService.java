package com.gmail.agentup.services;

import com.gmail.agentup.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer getCustomerById(Long id);

    List<Customer> getAllUserCustomers(Long userId);

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(Customer customer);
}
