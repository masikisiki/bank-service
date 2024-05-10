package com.example.sfassesment.services;

import com.example.sfassesment.dto.BankTransactionEvent;

public interface EventPublisherService {
    void publish(BankTransactionEvent eventJson);
}
