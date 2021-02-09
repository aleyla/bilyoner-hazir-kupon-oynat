package com.bilyoner.assignment.couponapi.entity;

import com.bilyoner.assignment.couponapi.model.enums.CouponStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatusEnum status;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime playDate;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "coupon", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponSelectionEntity> selectionEntities = new ArrayList<>();

    public void addSelection(CouponSelectionEntity couponSelectionEntity) {
        selectionEntities.add(couponSelectionEntity);
        couponSelectionEntity.setCoupon(this);
    }
}
