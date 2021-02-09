package com.bilyoner.assignment.couponapi.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequest {

    private List<Long> eventIds;
}
