package com.gmail.agentup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Currencies")
public class Currency {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserOwner userOwner;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    private List<Exchange> exchanges = new ArrayList<>();

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Currency(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public Currency() {
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setCurrency(this);
    }

    public void addExchange(Exchange exchange) {
        exchanges.add(exchange);
        exchange.setCurrency(this);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setCurrency(this);
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

        public UserOwner getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(UserOwner userOwner) {
        this.userOwner = userOwner;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
