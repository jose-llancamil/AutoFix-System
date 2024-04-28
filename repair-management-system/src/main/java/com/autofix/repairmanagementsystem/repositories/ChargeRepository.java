package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.ChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRepository extends JpaRepository<ChargeEntity, Long> {
}