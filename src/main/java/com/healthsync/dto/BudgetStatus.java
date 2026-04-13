package com.healthsync.dto;

public enum BudgetStatus {
    SAFE("安全", "success"),
    CAUTION("注意", "warning"),
    WARNING("预警", "orange"),
    EXCEEDED("超标", "danger");

    private final String label;
    private final String cssClass;

    BudgetStatus(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() { return label; }
    public String getCssClass() { return cssClass; }
}
