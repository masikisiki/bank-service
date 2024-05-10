package com.example.sfassesment.controllers;

import com.example.sfassesment.dto.WithdrawalResult;
import com.example.sfassesment.services.impl.BankServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    private final BankServiceService bankServiceService;

    public BankAccountController(BankServiceService bankServiceService) {
        this.bankServiceService = bankServiceService;
    }

    @PostMapping("/withdraw")
    public WithdrawalResult withdraw(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount) {
        return bankServiceService.withdraw(accountId, amount);
    }


    // We could clean this, move it outside the controller and use a @ControllerAdvice
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

