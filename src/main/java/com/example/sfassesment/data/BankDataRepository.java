package com.example.sfassesment.data;

import java.math.BigDecimal;

public interface BankDataRepository {
    BigDecimal getBalance(Long accountId);

    int updateAccountBalance(Long accountId, BigDecimal expectedNewBalance);

    boolean accountExists(Long accountId);
}
