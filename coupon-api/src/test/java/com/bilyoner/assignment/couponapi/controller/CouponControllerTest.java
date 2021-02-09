package com.bilyoner.assignment.couponapi.controller;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.repository.CouponRepository;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import com.bilyoner.assignment.couponapi.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CouponControllerTest {

    @Autowired
    private CouponController couponController;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private BalanceService balanceService;

    @Test
    public void can_getAllCouponsByCouponStatus() {
        EventEntity eventEntity = createEvent(LocalDateTime.now(), "test", EventTypeEnum.TENNIS);
        EventEntity eventEntity2 = createEvent(LocalDateTime.now(), "test2", EventTypeEnum.TENNIS);
        couponController.createCoupon(CouponCreateRequest.builder().eventIds(Arrays.asList(eventEntity.getId(), eventEntity2.getId())).build());
        couponController.createCoupon(CouponCreateRequest.builder().eventIds(Arrays.asList(eventEntity.getId(), eventEntity2.getId())).build());
        assertEquals(2, couponController.getAllCouponsByCouponStatus(CouponStatusEnum.CREATED).size());
    }

    private EventEntity createEvent(LocalDateTime time, String name, EventTypeEnum type) {
        EventEntity eventEntity = EventEntity.builder().name(name).mbs(1).type(type).eventDate(time.plusHours(1)).build();
        return eventRepository.save(eventEntity);
    }

    @Test
    public void can_createCoupon() {
        EventEntity eventEntity = createEvent(LocalDateTime.now(), "test", EventTypeEnum.TENNIS);
        EventEntity eventEntity2 = createEvent(LocalDateTime.now(), "test2", EventTypeEnum.TENNIS);
        CouponDTO coupon = couponController.createCoupon(
                CouponCreateRequest.builder().eventIds(Arrays.asList(eventEntity.getId(), eventEntity2.getId())).build());
        assertEquals(CouponStatusEnum.CREATED, coupon.getStatus());
    }

    @Test
    public void can_PlayCoupon() {
        EventEntity eventEntity = createEvent(LocalDateTime.now(), "test", EventTypeEnum.TENNIS);
        EventEntity eventEntity2 = createEvent(LocalDateTime.now(), "test2", EventTypeEnum.TENNIS);
        CouponDTO coupon = couponController.createCoupon(
                CouponCreateRequest.builder().eventIds(Arrays.asList(eventEntity.getId(), eventEntity2.getId())).build());
        when(balanceService.getUserAmount(1L)).thenReturn(BigDecimal.TEN);

        couponController.playCoupons(CouponPlayRequest.builder().userId(1L).couponIds(Collections.singletonList(coupon.getId())).build());

        Optional<CouponEntity> played = couponRepository.findById(coupon.getId());
        assertEquals(CouponStatusEnum.PLAYED, played.get().getStatus());

    }

    @Test
    public void can_getPlayedCoupons() {
        EventEntity eventEntity = createEvent(LocalDateTime.now(), "test", EventTypeEnum.TENNIS);
        EventEntity eventEntity2 = createEvent(LocalDateTime.now(), "test2", EventTypeEnum.TENNIS);
        CouponDTO coupon = couponController.createCoupon(
                CouponCreateRequest.builder().eventIds(Arrays.asList(eventEntity.getId(), eventEntity2.getId())).build());
        when(balanceService.getUserAmount(9L)).thenReturn(BigDecimal.TEN);

        List<CouponDTO> couponDTOS = couponController.playCoupons(
                CouponPlayRequest.builder().userId(9L).couponIds(Collections.singletonList(coupon.getId())).build());

        List<CouponDTO> playedCoupons = couponController.getPlayedCoupons(9L);
        assertEquals(playedCoupons.size(), couponDTOS.size());
    }

    @Test
    public void can_cancelCoupon() {
        EventEntity eventEntity = createEvent(LocalDateTime.now(), "test", EventTypeEnum.TENNIS);
        EventEntity eventEntity2 = createEvent(LocalDateTime.now(), "test2", EventTypeEnum.TENNIS);
        CouponDTO coupon = couponController.createCoupon(
                CouponCreateRequest.builder().eventIds(Arrays.asList(eventEntity.getId(), eventEntity2.getId())).build());
        when(balanceService.getUserAmount(9L)).thenReturn(BigDecimal.TEN);

        List<CouponDTO> couponDTOS = couponController.playCoupons(
                CouponPlayRequest.builder().userId(9L).couponIds(Collections.singletonList(coupon.getId())).build());
        CouponDTO couponDTO = couponDTOS.get(0);
        assertEquals(CouponStatusEnum.PLAYED, couponDTO.getStatus());

        CouponDTO cancelCoupon = couponController.cancelCoupon(couponDTO.getId());
        assertEquals(CouponStatusEnum.CREATED, cancelCoupon.getStatus());
    }
}
