package com.bilyoner.assignment.balanceapi.persistence.repository;

import com.bilyoner.assignment.balanceapi.persistence.entity.UserBalanceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBalanceHistoryRepository extends JpaRepository<UserBalanceHistoryEntity, Long> {

    Optional<UserBalanceHistoryEntity> findFirstByUserIdOrderByCreateDateDesc(Long userId);
}
