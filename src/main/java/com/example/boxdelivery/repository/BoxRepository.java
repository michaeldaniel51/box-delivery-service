package com.example.boxdelivery.repository;

import com.example.boxdelivery.entity.Box;
import com.example.boxdelivery.entity.BoxState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByTxref(String txref);
    List<Box> findByState(BoxState state);
    List<Box> findByStateAndBatteryCapacityGreaterThanEqual(BoxState state, int minBattery);

    boolean existsByTxref(String txref);

}
