package com.kosta.ems.benefit.enums;

public enum Calculation {
    MEAL_AMOUNT(5000),
    COMPLETION_RATE(0.8),
    TRAINING_AMOUNT(200000),
    LEAVE_CONVERSION_COUNT(3);

    private final double value;

    Calculation(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
