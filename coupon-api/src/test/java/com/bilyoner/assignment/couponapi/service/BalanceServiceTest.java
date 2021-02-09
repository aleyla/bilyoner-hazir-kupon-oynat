package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.exception.CouponApiException;
import com.bilyoner.assignment.couponapi.exception.ErrorCodeEnum;
import com.bilyoner.assignment.couponapi.service.clients.BalanceApiRestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {

    @InjectMocks
    private BalanceService service;

    @Mock
    private BalanceApiRestClient client;

    @Test
    public void should_not_get_user_amount() {
        when(client.getUserAmount(1L)).thenThrow(new CouponApiException(ErrorCodeEnum.INTERNAL_SERVER_ERROR));
        Assertions.assertThrows(CouponApiException.class, () -> {
            service.getUserAmount(1L);
        });
    }

}
