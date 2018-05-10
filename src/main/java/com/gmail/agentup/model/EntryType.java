package com.gmail.agentup.model;

public enum EntryType {
    GENERAL, TRANSFER;

    @Override
    public String toString() {
        return name();
    }
}
