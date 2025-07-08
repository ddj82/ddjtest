package com.example.ddjtest.repository;

import com.example.ddjtest.entity.PointCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointChargeRepository extends JpaRepository<PointCharge, Long> {
    Optional<PointCharge> findByOrderId(String orderId);
}
