package com.bilyoner.assignment.balanceapi.controller;

import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.model.UserBalanceDto;
import com.bilyoner.assignment.balanceapi.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "v1/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PutMapping
    public void updateBalance(@Valid @RequestBody UpdateBalanceRequest updateBalanceRequest) {
        balanceService.updateBalance(updateBalanceRequest);
    }

    @GetMapping(path = "{userId}")
    public UserBalanceDto getUserBalance(@PathVariable Long userId) {
        return balanceService.getByUserId(userId);
    }
}
