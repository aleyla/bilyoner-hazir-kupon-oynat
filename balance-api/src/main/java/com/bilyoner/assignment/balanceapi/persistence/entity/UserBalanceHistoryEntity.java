package com.bilyoner.assignment.balanceapi.persistence.entity;

import com.bilyoner.assignment.balanceapi.model.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal amount;

    private BigDecimal currentTotalAmount;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createDate;

}
