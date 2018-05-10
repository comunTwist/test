package com.gmail.agentup.model;

public enum UserRole {
    SUPER, ADMIN, USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
