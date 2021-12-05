package ru.sharipov.domain.banknote;

import java.math.BigDecimal;

public class TenBanknote extends AbstractBanknote {
    @Override
    public BigDecimal getDenomination() {
        return new BigDecimal(10);
    }
}
