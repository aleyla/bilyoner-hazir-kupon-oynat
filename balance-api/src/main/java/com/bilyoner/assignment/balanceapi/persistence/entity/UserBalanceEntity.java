package com.bilyoner.assignment.balanceapi.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceEntity {

    @Id
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

}
