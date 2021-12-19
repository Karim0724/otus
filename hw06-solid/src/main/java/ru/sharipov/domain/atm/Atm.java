package ru.sharipov.domain.atm;

import ru.sharipov.domain.banknote.Banknote;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface Atm {
    void acceptBanknote(Banknote banknote);
    void acceptBanknotes(Collection<Banknote> banknotes);
    BigDecimal showBalance();
    List<Banknote> getMoney(BigDecimal moneyToGet);
}
