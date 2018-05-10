package com.gmail.agentup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double sum;

    @OneToOne(mappedBy = "defaultAccount")
    private UserOwner userDefaultAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserOwner userOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String name, double sum) {
        this.name = name;
        this.sum = sum;
    }

    public Account() {
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
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

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public UserOwner getUserDefaultAccount() {
        return userDefaultAccount;
    }

    public void setUserDefaultAccount(UserOwner userDefaultAccount) {
        this.userDefaultAccount = userDefaultAccount;
    }

    public UserOwner getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(UserOwner userOwner) {
        this.userOwner = userOwner;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
