package com.healthsync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feishu_push_logs")
public class FeishuPushLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String pushType;

    private LocalDateTime pushAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Boolean success = true;
    private String errorMsg;

    public FeishuPushLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPushType() { return pushType; }
    public void setPushType(String pushType) { this.pushType = pushType; }
    public LocalDateTime getPushAt() { return pushAt; }
    public void setPushAt(LocalDateTime pushAt) { this.pushAt = pushAt; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
}
