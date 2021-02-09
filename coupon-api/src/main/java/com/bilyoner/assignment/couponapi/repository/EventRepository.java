package com.bilyoner.assignment.couponapi.repository;

import com.bilyoner.assignment.couponapi.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
