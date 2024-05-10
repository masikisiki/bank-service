package com.example.sfassesment.exceptions;

public class BankTransactionException extends RuntimeException {
    public BankTransactionException(String errorOccurredWhileProcessingWithdrawal, Exception e) {
        super(errorOccurredWhileProcessingWithdrawal, e);
    }
}
