package ru.sharipov.domain.banknote;

import java.math.BigDecimal;

public class ThousandBanknote extends AbstractBanknote {
    @Override
    public BigDecimal getDenomination() {
        return new BigDecimal(1000);
    }
}
