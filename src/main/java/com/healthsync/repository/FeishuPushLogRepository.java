package com.healthsync.repository;

import com.healthsync.entity.FeishuPushLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeishuPushLogRepository extends JpaRepository<FeishuPushLog, Long> {

    Optional<FeishuPushLog> findFirstByPushTypeOrderByPushAtDesc(String pushType);
}
