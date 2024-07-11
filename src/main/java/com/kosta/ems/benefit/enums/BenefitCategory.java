package com.kosta.ems.benefit.enums;

public enum BenefitCategory {
    BENEFIT_CATEGORY_SEQ_TRAINING(6),
    BENEFIT_CATEGORY_SEQ_MEAL(5),
    BENEFIT_CATEGORY_SEQ_SETTLEMENT(7);

    private int benefitCategory;

    private BenefitCategory(int benefitCategory) {
        this.benefitCategory = benefitCategory;
    }

    public int getBenefitCategory() {
        return benefitCategory;
    }
}
