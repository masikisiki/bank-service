package com.example.sfassesment.data;

import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class BankDataRepositoryImpl implements BankDataRepository {

    private final JdbcTemplate jdbcTemplate;

    public BankDataRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
       /*
        DOT REALLY NEED THIS FOR THIS EXERCISE
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(sql, new Object[]{accountId}, BigDecimal.class);
       */
        return BigDecimal.TEN;
    }

    @Override
    public int updateAccountBalance(Long accountId, BigDecimal expectedNewBalance) {
        /*
        DOT REALLY NEED THIS FOR THIS EXERCISE
        sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
       */
        return 1;
    }

    @Override
    public boolean accountExists(Long accountId) {
        return true;
    }
}
