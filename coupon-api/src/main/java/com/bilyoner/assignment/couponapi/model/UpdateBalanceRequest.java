package com.bilyoner.assignment.couponapi.model;

import com.bilyoner.assignment.couponapi.model.enums.TransactionType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequest {

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String transactionId;

    @NotBlank
    private TransactionType transactionType;
}
