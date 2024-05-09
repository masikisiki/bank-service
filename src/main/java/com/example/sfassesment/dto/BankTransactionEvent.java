package com.example.sfassesment.dto;

import org.json.JSONObject;

public interface BankTransactionEvent {
    default String toJson() {
        JSONObject jo = new JSONObject(this);
        return jo.toString();
    }
}
