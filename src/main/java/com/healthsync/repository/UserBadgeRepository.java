package com.healthsync.repository;

import com.healthsync.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findAll();
    boolean existsByBadge_Code(String badgeCode);
    List<UserBadge> findByNotifiedFalse();
}