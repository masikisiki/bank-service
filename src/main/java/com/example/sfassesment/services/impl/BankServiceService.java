package com.example.sfassesment.services.impl;

import com.example.sfassesment.data.BankDataRepository;
import com.example.sfassesment.dto.WithdrawalEvent;
import com.example.sfassesment.dto.WithdrawalResult;
import com.example.sfassesment.exceptions.AccountNotFoundException;
import com.example.sfassesment.exceptions.InsufficientFundsException;
import com.example.sfassesment.services.EventPublisherService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankServiceService {

    private final BankDataRepository bankDataRepository;
    private final EventPublisherService eventPublisherService;

    BankServiceService(BankDataRepository bankDataRepository, EventPublisherService eventPublisherService) {
        this.bankDataRepository = bankDataRepository;
        this.eventPublisherService = eventPublisherService;
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
        var newBalance = bankDataRepository.getBalance(accountId).subtract(amount);
        bankDataRepository.updateAccountBalance(accountId, newBalance);
        eventPublisherService.publish(new WithdrawalEvent(amount, accountId, "SUCCESSFUL"));
        return new WithdrawalResult(accountId, amount, "Withdrawal successful");
    }
}
