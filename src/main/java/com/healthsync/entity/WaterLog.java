package com.healthsync.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_logs")
public class WaterLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime loggedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Integer amountMl;

    private LocalDate loggedDate;
    private String source = "MANUAL";

    public WaterLog() {}

    @PrePersist
    public void prePersist() {
        if (loggedDate == null && loggedAt != null) {
            loggedDate = loggedAt.toLocalDate();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
    public Integer getAmountMl() { return amountMl; }
    public void setAmountMl(Integer amountMl) { this.amountMl = amountMl; }
    public LocalDate getLoggedDate() { return loggedDate; }
    public void setLoggedDate(LocalDate loggedDate) { this.loggedDate = loggedDate; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
