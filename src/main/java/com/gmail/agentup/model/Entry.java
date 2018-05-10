package com.gmail.agentup.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Entries")
public class Entry {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private EntryType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserOwner userOwner;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Entry(String name, EntryType type) {
        this.name = name;
        this.type = type;
    }

    public Entry() {
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setEntry(this);
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

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
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
