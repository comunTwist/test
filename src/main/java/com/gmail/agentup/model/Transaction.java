package com.gmail.agentup.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    private Double sumIn;
    private Double sumOut;
    private Date date;
    private String comment;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserOwner userOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    public Transaction(double sumIn, double sumOut, Date date, String comment, TransactionType type) {
        this.sumIn = sumIn;
        this.sumOut = sumOut;
        this.date = date;
        this.comment = comment;
        this.type = type;
    }

    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSumOut() {
        return sumOut;
    }

    public void setSumOut(Double sumOut) {
        this.sumOut = sumOut;
    }

    public Double getSumIn() {
        return sumIn;
    }

    public void setSumIn(Double sumIn) {
        this.sumIn = sumIn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public UserOwner getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(UserOwner userOwner) {
        this.userOwner = userOwner;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }
}
