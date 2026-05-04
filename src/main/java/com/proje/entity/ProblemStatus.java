package com.proje.entity;

public enum ProblemStatus {
    PENDING("Beklemede"),
    APPROVED("Onaylandi"),
    REJECTED("Reddedildi");

    private final String label;

    ProblemStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
