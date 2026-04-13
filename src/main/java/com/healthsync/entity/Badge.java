package com.healthsync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "badges")
public class Badge {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 100)
    private String description;

    @Column(length = 20)
    private String category;

    private Integer threshold;

    @Column(length = 10)
    private String icon;

    @Column(nullable = false)
    private Boolean hidden = false;

    public Badge() {}

    public Badge(String code, String name, String description, String category, Integer threshold, String icon) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.threshold = threshold;
        this.icon = icon;
        this.hidden = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Boolean getHidden() { return hidden; }
    public void setHidden(Boolean hidden) { this.hidden = hidden; }
}