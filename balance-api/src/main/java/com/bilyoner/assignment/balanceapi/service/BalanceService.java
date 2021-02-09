package com.bilyoner.assignment.balanceapi.service;

import com.bilyoner.assignment.balanceapi.exception.BalanceApiException;
import com.bilyoner.assignment.balanceapi.exception.ErrorCodeEnum;
import com.bilyoner.assignment.balanceapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.balanceapi.model.UserBalanceDto;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceEntity;
import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceHistoryEntity;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceHistoryRepository;
import com.bilyoner.assignment.balanceapi.persistence.repository.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserBalanceRepository balanceRepository;

    private final UserBalanceHistoryRepository historyRepository;

    public void updateBalance(UpdateBalanceRequest request) {
        BigDecimal currentAmount = request.getAmount();
        if (currentAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BalanceApiException(ErrorCodeEnum.FIELD_VALIDATION_ERROR);
        }
        Optional<UserBalanceHistoryEntity> balanceHistory = historyRepository.findFirstByUserIdOrderByCreateDateDesc(request.getUserId());
        if (balanceHistory.isPresent()) {
            currentAmount = calculateUserCurrentAmount(request, balanceHistory.get());
        }
        balanceRepository.save(UserBalanceEntity.builder().userId(request.getUserId()).amount(currentAmount).build());

        historyRepository.save(UserBalanceHistoryEntity.builder()
                                                       .amount(request.getAmount())
                                                       .transactionId(request.getTransactionId())
                                                       .createDate(LocalDateTime.now())
                                                       .type(request.getTransactionType())
                                                       .currentTotalAmount(currentAmount)
                                                       .userId(request.getUserId())
                                                       .build());

    }

    private BigDecimal calculateUserCurrentAmount(UpdateBalanceRequest request, UserBalanceHistoryEntity balanceHistory) {
        BigDecimal balance = request.getAmount();
        BigDecimal oldAmount = balanceHistory.getCurrentTotalAmount();
        switch (request.getTransactionType()) {
        case WITHDRAW:
        case COUPON_FEE:
            if (oldAmount.compareTo(balance) < 0) {
                throw new BalanceApiException(ErrorCodeEnum.FIELD_VALIDATION_ERROR);
            }
            return oldAmount.subtract(balance);
        default:
            //DEPOSIT and REFUND
            return oldAmount.add(balance);
        }
    }

    public UserBalanceDto getByUserId(Long userId) {
        UserBalanceEntity userBalance = balanceRepository.findByUserId(userId)
                                                         .orElseThrow(new BalanceApiException(ErrorCodeEnum.CONTENT_NOT_FOUND_ERROR));
        return UserBalanceDto.builder().userId(userId).amount(userBalance.getAmount()).build();
    }
}
