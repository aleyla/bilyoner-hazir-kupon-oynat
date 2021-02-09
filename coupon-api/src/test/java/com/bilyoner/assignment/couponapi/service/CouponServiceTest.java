package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.exception.NotFoundException;
import com.bilyoner.assignment.couponapi.exception.ValidationException;
import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.repository.CouponRepository;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private CouponService service;

    private CouponCreateRequest createRequest;

    private CouponPlayRequest playRequest;

    private EventEntity eventEntity;

    @BeforeEach
    public void setUp() {
        createRequest = new CouponCreateRequest();
        eventEntity = new EventEntity();
        playRequest = new CouponPlayRequest();
    }

    @Test
    public void should_not_create_coupon_expired_event() {
        createRequest.setEventIds(Collections.singletonList(1L));
        eventEntity.setEventDate(LocalDateTime.now().minusDays(2));
        when(eventRepository.findAllById(Collections.singletonList(1L))).thenReturn(Collections.singletonList(eventEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            service.createCoupon(createRequest);
        });

    }

    @Test
    public void should_not_create_coupon_empty_event() {
        Assertions.assertThrows(ValidationException.class, () -> {
            service.createCoupon(createRequest);
        });

    }

    @Test
    public void should_not_create_coupon_event_byMbs() {
        createRequest.setEventIds(Collections.singletonList(1L));
        eventEntity.setEventDate(LocalDateTime.now().plusDays(2));
        eventEntity.setMbs(3);
        when(eventRepository.findAllById(Collections.singletonList(1L))).thenReturn(Collections.singletonList(eventEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            service.createCoupon(createRequest);
        });
    }

    @Test
    public void should_not_create_coupon_event_byType() {
        createRequest.setEventIds(Arrays.asList(1L, 2L));
        eventEntity.setEventDate(LocalDateTime.now().plusDays(2));
        eventEntity.setMbs(2);
        eventEntity.setType(EventTypeEnum.TENNIS);

        EventEntity eventEntity2 = new EventEntity();
        eventEntity2.setEventDate(LocalDateTime.now().plusDays(1));
        eventEntity2.setMbs(2);
        eventEntity2.setType(EventTypeEnum.FOOTBALL);
        when(eventRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(eventEntity2, eventEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            service.createCoupon(createRequest);
        });
    }

    @Test
    public void should_not_play_empty_coupon() {
        playRequest.setUserId(1L);
        playRequest.setCouponIds(Arrays.asList(1L, 2L));
        when(couponRepository.findAllByIdIsInAndStatus(Arrays.asList(1L, 2L), CouponStatusEnum.CREATED)).thenReturn(new ArrayList<>());
        Assertions.assertThrows(ValidationException.class, () -> {
            service.playCoupons(playRequest);
        });

    }

    @Test
    public void should_not_play_coupon_user_balance_not_enough() {
        playRequest.setUserId(1L);
        playRequest.setCouponIds(Collections.singletonList(1L));
        CouponEntity couponEntity = CouponEntity.builder().cost(new BigDecimal(5)).userId(1L).build();
        when(couponRepository.findAllByIdIsInAndStatus(playRequest.getCouponIds(), CouponStatusEnum.CREATED)).thenReturn(
                Collections.singletonList(couponEntity));
        when(balanceService.getUserAmount(playRequest.getUserId())).thenReturn(BigDecimal.ONE);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.playCoupons(playRequest);
        });
    }

    @Test
    public void should_not_cancel_not_founded_coupon() {
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> {
            service.cancelCoupon(1L);
        });

    }

    @Test
    public void should_not_cancel_coupon_already_not_played() {
        CouponEntity couponEntity = CouponEntity.builder().status(CouponStatusEnum.CREATED).userId(1L).build();
        when(couponRepository.findById(1L)).thenReturn(Optional.of(couponEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            service.cancelCoupon(1L);
        });

    }

    @Test
    public void should_not_cancel_coupon_time_expired() {
        CouponEntity couponEntity = CouponEntity.builder()
                                                .status(CouponStatusEnum.PLAYED)
                                                .playDate(LocalDateTime.now().minusMinutes(6))
                                                .userId(1L)
                                                .build();
        when(couponRepository.findById(1L)).thenReturn(Optional.of(couponEntity));
        Assertions.assertThrows(ValidationException.class, () -> {
            service.cancelCoupon(1L);
        });
    }

}
