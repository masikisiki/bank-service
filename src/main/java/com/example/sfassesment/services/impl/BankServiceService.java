package com.example.sfassesment.services.impl;

import com.example.sfassesment.data.BankDataRepository;
import com.example.sfassesment.dto.WithdrawalEvent;
import com.example.sfassesment.dto.WithdrawalResult;
import com.example.sfassesment.exceptions.AccountNotFoundException;
import com.example.sfassesment.exceptions.BankTransactionException;
import com.example.sfassesment.exceptions.InsufficientFundsException;
import com.example.sfassesment.services.EventPublisherService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BankServiceService {

    private final BankDataRepository bankDataRepository;
    private final EventPublisherService eventPublisherService;

    BankServiceService(BankDataRepository bankDataRepository, EventPublisherService eventPublisherService) {
        //lets fail fast if any of the dependencies are nulls
        this.bankDataRepository = Objects.requireNonNull(bankDataRepository, "BankDataRepository must not be null");
        this.eventPublisherService = Objects.requireNonNull(eventPublisherService, "EventPublisherService must not be null");
    }

    public WithdrawalResult withdraw(Long accountId, BigDecimal amount) {
        checkAccountExists(accountId);
        checkSufficientFunds(accountId, amount);
        return performWithdrawal(accountId, amount);
    }

    private void checkAccountExists(Long accountId) {
        if (!bankDataRepository.accountExists(accountId)) {
            throw new AccountNotFoundException("Account not found");
        }
    }

    private void checkSufficientFunds(Long accountId, BigDecimal amount) {
        var currentBalance = bankDataRepository.getBalance(accountId);
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
    }

    private WithdrawalResult performWithdrawal(Long accountId, BigDecimal amount) {
        try {
            var newBalance = bankDataRepository.getBalance(accountId).subtract(amount);
            var affectedRows = bankDataRepository.updateAccountBalance(accountId, newBalance);
            if(affectedRows != 1){
                throw new RuntimeException("Unexpected execution results");
            }
            eventPublisherService.publish(new WithdrawalEvent(amount, accountId, "SUCCESSFUL"));
            return new WithdrawalResult(accountId, amount, "Withdrawal successful");
        } catch (Exception e) {
            throw new BankTransactionException("Error occurred while processing withdrawal", e);
        }
    }
}
