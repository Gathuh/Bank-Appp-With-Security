package com.gathu.gathu.security.repository;

import com.gathu.gathu.security.entity.StandingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StandingOrderRepository extends JpaRepository<StandingOrder, Long> {
    List<StandingOrder> findByNextExecutionBefore(LocalDateTime now);
}