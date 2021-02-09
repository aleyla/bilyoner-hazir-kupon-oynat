package com.bilyoner.assignment.couponapi.repository;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    List<CouponEntity> findAllByStatus(CouponStatusEnum status);

    List<CouponEntity> findAllByUserId(Long userId);

    List<CouponEntity> findAllByIdIsInAndStatus(List<Long> id, CouponStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE CouponEntity  c SET c.playDate = CURRENT_TIMESTAMP, c.status = 'PLAYED', c.userId = :userId WHERE c.id in (:ids) ")
    void updateUserCouponsStatusToPayed(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE CouponEntity  c SET c.playDate = null, c.status = 'CREATED', c.userId = null WHERE c.id in (:ids) ")
    void updateCouponsStatusToCreated(@Param("ids") List<Long> playedCouponsIds);
}
