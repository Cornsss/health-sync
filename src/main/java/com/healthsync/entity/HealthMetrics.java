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

    /** 体重 kg（目标 55kg）*/
    private BigDecimal weightKg;

    /** 体脂率 %（智能体脂秤可测）*/
    private BigDecimal bodyFatPct;

    /** 尿酸 μmol/L（正常 < 420，抽血检测）*/
    private Integer uricAcid;

    /** 谷丙转氨酶 ALT U/L（正常 < 40，抽血检测）*/
    private BigDecimal alt;

    /** 谷草转氨酶 AST U/L（抽血检测）*/
    private BigDecimal ast;

    /** 谷氨酰转移酶 GGT U/L（脂肪肝敏感，正常 < 45，抽血检测）*/
    private BigDecimal ggt;

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
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
