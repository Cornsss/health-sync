package com.healthsync.repository;

import com.healthsync.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByCode(String code);
}