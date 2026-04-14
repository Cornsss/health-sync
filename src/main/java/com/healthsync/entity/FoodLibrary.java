package com.healthsync.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "food_library")
public class FoodLibrary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private BigDecimal purinePerHundredG = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal fatPerHundredG = BigDecimal.ZERO;

    private BigDecimal fructosePerHundredG = BigDecimal.ZERO;
    private BigDecimal caloriesPerHundredG = BigDecimal.ZERO;
    private Boolean allowedForUricAcid = true;

    @Column(columnDefinition = "TEXT")
    private String note;

    private String seasons;

    public FoodLibrary() {}

    @Transient
    public boolean isLowPurine() {
        return purinePerHundredG.compareTo(new BigDecimal("25")) < 0;
    }

    @Transient
    public boolean isLowFat() {
        return fatPerHundredG.compareTo(new BigDecimal("3")) < 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPurinePerHundredG() { return purinePerHundredG; }
    public void setPurinePerHundredG(BigDecimal v) { this.purinePerHundredG = v; }
    public BigDecimal getFatPerHundredG() { return fatPerHundredG; }
    public void setFatPerHundredG(BigDecimal v) { this.fatPerHundredG = v; }
    public BigDecimal getFructosePerHundredG() { return fructosePerHundredG; }
    public void setFructosePerHundredG(BigDecimal v) { this.fructosePerHundredG = v; }
    public BigDecimal getCaloriesPerHundredG() { return caloriesPerHundredG; }
    public void setCaloriesPerHundredG(BigDecimal v) { this.caloriesPerHundredG = v; }
    public Boolean getAllowedForUricAcid() { return allowedForUricAcid; }
    public void setAllowedForUricAcid(Boolean v) { this.allowedForUricAcid = v; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getSeasons() { return seasons; }
    public void setSeasons(String seasons) { this.seasons = seasons; }
}
