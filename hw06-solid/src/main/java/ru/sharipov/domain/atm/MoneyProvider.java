package ru.sharipov.domain.atm;

import ru.sharipov.domain.banknote.Banknote;

import java.math.BigDecimal;
import java.util.List;

public interface MoneyProvider {
    List<Banknote> getMoney(BigDecimal moneyToGet);
}
