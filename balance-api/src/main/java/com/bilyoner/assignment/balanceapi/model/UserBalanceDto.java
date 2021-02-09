package com.bilyoner.assignment.balanceapi.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class UserBalanceDto implements Serializable {

    private Long userId;

    private BigDecimal amount;
}
