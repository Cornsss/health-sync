package com.healthsync.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "health_metrics")
public class HealthMetrics {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recordedAt = LocalDate.now();

    private BigDecimal weightKg;
    private BigDecimal bodyFatPct;
    private Integer uricAcid;
    private BigDecimal alt;
    private BigDecimal ast;
    private BigDecimal ggt;
    private BigDecimal kidneyCrystalMm;
    private BigDecimal creatinine;

    @Column(columnDefinition = "TEXT")
    private String note;

    public HealthMetrics() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDate recordedAt) { this.recordedAt = recordedAt; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public BigDecimal getBodyFatPct() { return bodyFatPct; }
    public void setBodyFatPct(BigDecimal bodyFatPct) { this.bodyFatPct = bodyFatPct; }
    public Integer getUricAcid() { return uricAcid; }
    public void setUricAcid(Integer uricAcid) { this.uricAcid = uricAcid; }
    public BigDecimal getAlt() { return alt; }
    public void setAlt(BigDecimal alt) { this.alt = alt; }
    public BigDecimal getAst() { return ast; }
    public void setAst(BigDecimal ast) { this.ast = ast; }
    public BigDecimal getGgt() { return ggt; }
    public void setGgt(BigDecimal ggt) { this.ggt = ggt; }
    public BigDecimal getKidneyCrystalMm() { return kidneyCrystalMm; }
    public void setKidneyCrystalMm(BigDecimal kidneyCrystalMm) { this.kidneyCrystalMm = kidneyCrystalMm; }
    public BigDecimal getCreatinine() { return creatinine; }
    public void setCreatinine(BigDecimal creatinine) { this.creatinine = creatinine; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
