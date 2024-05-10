package com.example.sfassesment.services.impl;

import com.example.sfassesment.data.BankDataRepository;
import com.example.sfassesment.exceptions.InsufficientFundsException;
import com.example.sfassesment.services.EventPublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BankServiceServiceTest {
    private final Long accountId = 1L;

    private BankDataRepository repository;
    private EventPublisherService publisher;

    @BeforeEach
    void beforeEach() {
        repository = Mockito.mock(BankDataRepository.class);
        publisher = Mockito.mock(EventPublisherService.class);

        when(repository.updateAccountBalance(anyLong(),any(BigDecimal.class))).thenReturn(1);
        when(repository.accountExists(anyLong())).thenReturn(true);
    }

    @Test
    void withdraw_WhenBalanceIsLessThankWithdrawalAmount_Fail() {
        // Arrange
        when(repository.getBalance(anyLong())).thenReturn(BigDecimal.valueOf(100));

        BankServiceService bankServiceService = new BankServiceService(repository, publisher);
        var withdrawalAmount = 2000L;

        // Act
        var exception = assertThrows(InsufficientFundsException.class, () ->
                bankServiceService.withdraw(accountId, BigDecimal.valueOf(withdrawalAmount)));

        // Assert
        assertEquals("Insufficient funds for withdrawal", exception.getMessage());
        verify(repository, times(1)).getBalance(accountId);
    }

    @Test
    void withdraw_WhenBalanceEqualWithdrawalAmount_Pass() {
        // Arrange
        when(repository.getBalance(anyLong())).thenReturn(BigDecimal.valueOf(100));
        BankServiceService bankServiceService = new BankServiceService(repository, publisher);
        var withdrawalAmount = BigDecimal.valueOf(100);

        // Act
        var actual = bankServiceService.withdraw(accountId, withdrawalAmount);

        // Assert
        assertEquals("Withdrawal successful", actual.result());
        verify(repository, atLeastOnce()).getBalance(accountId);
        verify(publisher, times(1)).publish(any());
    }

    @Test
    void withdraw_WhenBalanceIsMoreThanWithdrawalAmount_Pass() {
        // Arrange
        when(repository.getBalance(anyLong())).thenReturn(BigDecimal.valueOf(10000));

        BankServiceService bankServiceService = new BankServiceService(repository, publisher);
        var withdrawalAmount = BigDecimal.valueOf(100);

        // Act
        var actual = bankServiceService.withdraw(accountId, withdrawalAmount);

        // Assert
        assertEquals("Withdrawal successful", actual.result());
        verify(repository, atLeastOnce()).getBalance(accountId);
        verify(publisher, times(1)).publish(any());
    }


    @CsvSource({
            "1000, 1000",
            "5000, 100",
            "10, 5"
    })
    @ParameterizedTest
    void withdraw_WhenWithdrawalIsSuccessful_UpdateAccountBalance(BigDecimal balance, BigDecimal withdrawalAmount) {
        // Arrange
        when(repository.getBalance(anyLong())).thenReturn(balance);
        BankServiceService bankServiceService = new BankServiceService(repository, publisher);
        var expectedNewBalance = balance.subtract(withdrawalAmount);

        // Act
        var actual = bankServiceService.withdraw(accountId, withdrawalAmount);

        // Assert
        assertEquals("Withdrawal successful", actual.result());
        verify(repository, atLeastOnce()).getBalance(accountId);
        verify(repository, times(1)).updateAccountBalance(accountId, expectedNewBalance);
    }
}
