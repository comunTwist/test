package com.gmail.agentup.services;

import com.gmail.agentup.model.Customer;
import com.gmail.agentup.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long id) {
        return customerRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllUserCustomers(Long userId) {
        return customerRepository.findAllUserCustomers(userId);
    }

    @Override
    @Transactional
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
