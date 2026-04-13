package com.healthsync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_badges")
public class UserBadge {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime unlockedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    private Boolean notified = false;

    public UserBadge() {}

    public UserBadge(Badge badge) {
        this.badge = badge;
        this.unlockedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(LocalDateTime unlockedAt) { this.unlockedAt = unlockedAt; }
    public Badge getBadge() { return badge; }
    public void setBadge(Badge badge) { this.badge = badge; }
    public Boolean getNotified() { return notified; }
    public void setNotified(Boolean notified) { this.notified = notified; }
}