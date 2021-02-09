package com.bilyoner.assignment.couponapi.model;

import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CouponDTO implements Serializable {

    private Long id;
    private Long userId;
    private CouponStatusEnum status;
    private BigDecimal cost;
    private List<Long> eventIds;
    private LocalDateTime playDate;
}
