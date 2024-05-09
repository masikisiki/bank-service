package com.example.sfassesment.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String insufficientFundsForWithdrawal) {
        super(insufficientFundsForWithdrawal);
    }
}
