package com.example.sfassesment.data;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class BankDataRepositoryImpl implements BankDataRepository {

    @Override
    public BigDecimal getBalance(Long accountId) {
        return BigDecimal.TEN;
    }

    @Override
    public int updateAccountBalance(Long accountId, BigDecimal expectedNewBalance) {
        return 1;
    }

    @Override
    public boolean accountExists(Long accountId) {
        return true;
    }
}
