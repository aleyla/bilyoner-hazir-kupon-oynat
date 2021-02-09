package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.couponapi.model.enums.TransactionType;
import com.bilyoner.assignment.couponapi.service.clients.BalanceApiRestClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BalanceService {

    private final BalanceApiRestClient client;

    public BigDecimal getUserAmount(Long userId) {
        return client.getUserAmount(userId).getAmount();
    }

    public void updateBalance(Long userId, BigDecimal sum, TransactionType type) {
        client.updateBalance(
                UpdateBalanceRequest.builder().userId(userId).amount(sum).transactionId(getTransactionId()).transactionType(type).build());

    }

    private String getTransactionId() {
        return UUID.randomUUID().toString();
    }
}
