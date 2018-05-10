package com.gmail.agentup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class UserOwner {
    @Id
    @GeneratedValue
    private Long id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer defaultCustomer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account defaultAccount;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    private List<Currency> currencies = new ArrayList<>();

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();


    public UserOwner(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public UserOwner() {
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setUserOwner(this);
    }

    public void addCurrency(Currency currency) {
        currencies.add(currency);
        currency.setUserOwner(this);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        customer.setUserOwner(this);
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
        entry.setUserOwner(this);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setUserOwner(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Customer getDefaultCustomer() {
        return defaultCustomer;
    }

    public void setDefaultCustomer(Customer defaultCustomer) {
        this.defaultCustomer = defaultCustomer;
    }

    public Account getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(Account defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
