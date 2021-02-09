package com.bilyoner.assignment.couponapi.controller;

import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public List<CouponDTO> getAllCouponsByCouponStatus(@RequestParam CouponStatusEnum couponStatus) {
        return couponService.getAllCouponsByCouponStatus(couponStatus);
    }

    @PostMapping
    public CouponDTO createCoupon(@RequestBody @Valid CouponCreateRequest couponCreateRequest) {
        return couponService.createCoupon(couponCreateRequest);
    }

    @GetMapping(path = "/played-by-user/{userId}")
    public List<CouponDTO> getPlayedCoupons(@PathVariable Long userId) {
        return couponService.getPlayedCoupons(userId);
    }

    @PostMapping(path = "/play")
    public List<CouponDTO> playCoupons(@Valid @RequestBody CouponPlayRequest couponPlayRequest) {
        return couponService.playCoupons(couponPlayRequest);
    }

    @PatchMapping(path = "/{couponId}/cancel")
    public CouponDTO cancelCoupon(@PathVariable Long couponId) {
        return couponService.cancelCoupon(couponId);
    }
}