package com.bilyoner.assignment.balanceapi.persistence.repository;

import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBalanceRepository extends JpaRepository<UserBalanceEntity, Long> {

    Optional<UserBalanceEntity> findByUserId(Long userId);
}
