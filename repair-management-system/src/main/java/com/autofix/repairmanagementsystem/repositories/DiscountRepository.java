package com.autofix.repairmanagementsystem.repositories;

import com.autofix.repairmanagementsystem.entities.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
}