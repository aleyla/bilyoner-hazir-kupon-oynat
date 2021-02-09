package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponDTO toCouponBase(CouponEntity couponEntity);

    default CouponDTO toCouponDTO(CouponEntity couponEntity) {
        CouponDTO dto = toCouponBase(couponEntity);
        dto.setEventIds(couponEntity.getSelectionEntities().stream().map(c -> c.getEvent().getId()).collect(Collectors.toList()));
        return dto;
    }

}
