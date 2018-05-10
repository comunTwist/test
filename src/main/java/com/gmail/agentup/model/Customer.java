package com.gmail.agentup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToOne(mappedBy = "defaultCustomer")
    private UserOwner userDefaultCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserOwner userOwner;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Customer(String name) {
        this.name = name;
    }

    public Customer() {
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setCustomer(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserOwner getUserDefaultCustomer() {
        return userDefaultCustomer;
    }

    public void setUserDefaultCustomer(UserOwner userDefaultCustomer) {
        this.userDefaultCustomer = userDefaultCustomer;
    }

    public UserOwner getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(UserOwner userOwner) {
        this.userOwner = userOwner;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
