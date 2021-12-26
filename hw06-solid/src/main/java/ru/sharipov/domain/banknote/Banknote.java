package ru.sharipov.domain.banknote;

import java.math.BigDecimal;

public record Banknote(BigDecimal denomination) {
    public Banknote(Integer denomination) {
        this(BigDecimal.valueOf(denomination));
    }

    public BigDecimal getDenomination() {
        return denomination;
    }
}
