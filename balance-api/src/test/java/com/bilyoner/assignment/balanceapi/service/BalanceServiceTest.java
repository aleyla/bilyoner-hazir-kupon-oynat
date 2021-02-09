package com.bilyoner.assignment.balanceapi.service;

import com.bilyoner.assignment.balanceapi.exception.BalanceApiException;
import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.model.enums.TransactionType;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceEntity;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceHistoryEntity;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceHistoryRepository;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private UserBalanceRepository balanceRepository;

    @Mock
    private UserBalanceHistoryRepository historyRepository;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    void should_not_get_not_founded_user_balance() {
        Assertions.assertThrows(BalanceApiException.class, () -> {
            balanceService.getByUserId(1L);
        });
    }

    @Test
    void should_not_add_negative_user_balance() {
        UpdateBalanceRequest request = UpdateBalanceRequest.builder().amount(new BigDecimal(-10)).build();
        Assertions.assertThrows(BalanceApiException.class, () -> {
            balanceService.updateBalance(request);
        });
    }

    @Test
    void should_not_update_negative_user_balance() {
        UpdateBalanceRequest request = UpdateBalanceRequest.builder()
                                                           .amount(new BigDecimal(10))
                                                           .transactionType(TransactionType.COUPON_FEE)
                                                           .userId(1L)
                                                           .build();

        UserBalanceEntity balanceEntity = UserBalanceEntity.builder().userId(1L).build();
        UserBalanceHistoryEntity balanceHistoryEntity = UserBalanceHistoryEntity.builder().currentTotalAmount(new BigDecimal(2L)).build();
        lenient().when(balanceRepository.findByUserId(balanceEntity.getUserId())).thenReturn(Optional.of(balanceEntity));
        lenient().when(historyRepository.findFirstByUserIdOrderByCreateDateDesc(request.getUserId())).thenReturn(Optional.of(balanceHistoryEntity));

        Assertions.assertThrows(BalanceApiException.class, () -> {
            balanceService.updateBalance(request);
        });
    }

}