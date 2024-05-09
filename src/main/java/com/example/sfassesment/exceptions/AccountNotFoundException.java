package com.example.sfassesment.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNotFound) {
        super(accountNotFound);
    }
}
