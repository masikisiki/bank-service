package com.example.sfassesment.dto;

import java.math.BigDecimal;

public record WithdrawalResult(long accountId, BigDecimal withdrawalAmount, String result) {
}
