package com.bilyoner.assignment.couponapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponPlayRequest {

    @NotNull
    private Long userId;

    @NotNull
    private List<Long> couponIds;
}
