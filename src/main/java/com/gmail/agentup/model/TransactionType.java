package com.gmail.agentup.model;

public enum TransactionType {
    ZERO, LOSS, PROFIT, TRANSFER;

    @Override
    public String toString() {
        return name();
    }
}
