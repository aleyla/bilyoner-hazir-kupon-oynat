package com.bilyoner.assignment.couponapi.service;

import com.bilyoner.assignment.couponapi.entity.CouponEntity;
import com.bilyoner.assignment.couponapi.entity.CouponSelectionEntity;
import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.exception.NotFoundException;
import com.bilyoner.assignment.couponapi.exception.UnexpectedException;
import com.bilyoner.assignment.couponapi.exception.ValidationException;
import com.bilyoner.assignment.couponapi.model.CouponCreateRequest;
import com.bilyoner.assignment.couponapi.model.CouponDTO;
import com.bilyoner.assignment.couponapi.model.CouponPlayRequest;
import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.bilyoner.assignment.couponapi.model.enums.TransactionType;
import com.bilyoner.assignment.couponapi.repository.CouponRepository;
import com.bilyoner.assignment.couponapi.repository.CouponSelectionEntityRepository;
import com.bilyoner.assignment.couponapi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    public static final int MINUTES = 5;

    public static final BigDecimal COST = new BigDecimal(5);

    private final CouponRepository couponRepository;

    private final EventRepository eventRepository;

    private final CouponSelectionEntityRepository selectionRepository;

    private final BalanceService balanceService;

    private final CouponMapper mapper;

    public List<CouponDTO> getAllCouponsByCouponStatus(CouponStatusEnum couponStatus) {
        return couponRepository.findAllByStatus(couponStatus).stream().map(mapper::toCouponDTO).collect(Collectors.toList());
    }

    @Transactional
    public CouponDTO createCoupon(CouponCreateRequest couponCreateRequest) {
        if (couponCreateRequest.getEventIds() == null || couponCreateRequest.getEventIds().isEmpty()) {
            throw new ValidationException("Please select events");
        }
        List<EventEntity> events = eventRepository.findAllById(couponCreateRequest.getEventIds());
        List<CouponSelectionEntity> couponSelections = validatedCouponSelections(events);
        CouponEntity couponEntity = new CouponEntity();
        LocalDateTime now = LocalDateTime.now();

        if (!couponSelections.isEmpty()) {
            couponEntity.setStatus(CouponStatusEnum.CREATED);
            couponEntity.setCost(COST);
            couponEntity.setCreateDate(now);
            couponEntity.setUpdateDate(now);
            couponEntity = couponRepository.save(couponEntity);
            for (CouponSelectionEntity couponSelectionEntity : couponSelections) {
                couponSelectionEntity.setCoupon(couponEntity);
                couponSelectionEntity.setCreateDate(LocalDateTime.now());
                selectionRepository.save(couponSelectionEntity);
                couponEntity.addSelection(couponSelectionEntity);
            }
        }

        return mapper.toCouponDTO(couponEntity);
    }

    private List<CouponSelectionEntity> validatedCouponSelections(List<EventEntity> events) {
        List<CouponSelectionEntity> couponSelections = new ArrayList<>();
        boolean hasFootball = false;
        boolean hasTennis = false;
        int eventsCount = events.size();
        for (EventEntity eventEntity : events) {
            if (eventEntity.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Played events can not play!");
            }
            if (eventsCount < eventEntity.getMbs()) {
                throw new ValidationException("You should add more events!");
            }
            if (EventTypeEnum.FOOTBALL.equals(eventEntity.getType())) {
                hasFootball = true;
            }
            if (EventTypeEnum.TENNIS.equals(eventEntity.getType())) {
                hasTennis = true;
            }
            if (hasFootball && hasTennis) {
                throw new ValidationException("FOOTBALL and TENNIS  events can not play together");
            }

            CouponSelectionEntity selectionEntity = new CouponSelectionEntity();
            selectionEntity.setEvent(eventEntity);
            couponSelections.add(selectionEntity);
        }
        return couponSelections;
    }

    public List<CouponDTO> playCoupons(CouponPlayRequest couponPlayRequest) {
        if (couponPlayRequest.getCouponIds() == null || couponPlayRequest.getUserId() == null) {
            throw new ValidationException("Please check your request!");
        }
        List<CouponEntity> coupons = couponRepository.findAllByIdIsInAndStatus(couponPlayRequest.getCouponIds(), CouponStatusEnum.CREATED);
        if (coupons.isEmpty()) {
            throw new ValidationException("Please check your coupons!");
        }
        BigDecimal sum = coupons.stream().map(CouponEntity::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal userBalance = balanceService.getUserAmount(couponPlayRequest.getUserId());
        if (sum.compareTo(userBalance) > 0) {
            throw new ValidationException("User balance is insufficient!");
        }

        List<Long> couponsIds = coupons.stream().map(CouponEntity::getId).collect(Collectors.toList());
        try {
            balanceService.updateBalance(couponPlayRequest.getUserId(), sum, TransactionType.COUPON_FEE);
            couponRepository.updateUserCouponsStatusToPayed(couponsIds, couponPlayRequest.getUserId());
            return couponRepository.findAllByIdIsInAndStatus(couponsIds, CouponStatusEnum.PLAYED)
                                   .stream()
                                   .map(mapper::toCouponDTO)
                                   .collect(Collectors.toList());
        }
        catch (Exception e) {
            couponRepository.updateCouponsStatusToCreated(couponsIds);
            balanceService.updateBalance(couponPlayRequest.getUserId(), sum, TransactionType.EXCEPTION);
            throw new UnexpectedException("Please try again later!");
        }
    }

    public CouponDTO cancelCoupon(Long couponId) {
        CouponEntity couponEntity = validateCancelation(couponId);
        couponEntity.setStatus(CouponStatusEnum.CREATED);
        couponEntity.setUserId(null);
        couponEntity.setPlayDate(null);
        balanceService.updateBalance(couponEntity.getUserId(), couponEntity.getCost(), TransactionType.REFUND);
        return mapper.toCouponDTO(couponRepository.save(couponEntity));
    }

    private CouponEntity validateCancelation(Long couponId) {
        CouponEntity couponEntity = couponRepository.findById(couponId).orElseThrow(new NotFoundException(String.valueOf(couponId)));
        if (couponEntity.getPlayDate() == null || !couponEntity.getStatus().equals(CouponStatusEnum.PLAYED)) {
            throw new ValidationException("Coupon already not played!");
        }
        if (couponEntity.getPlayDate().plusMinutes(MINUTES).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cancelation time is over!");
        }
        return couponEntity;
    }

    public List<CouponDTO> getPlayedCoupons(Long userId) {
        return couponRepository.findAllByUserId(userId).stream().map(mapper::toCouponDTO).collect(Collectors.toList());
    }
}
