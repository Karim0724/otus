package ru.sharipov.domain.banknote;

import java.math.BigDecimal;

public class HundredBanknote extends AbstractBanknote {
    @Override
    public BigDecimal getDenomination() {
        return new BigDecimal(100);
    }
}
