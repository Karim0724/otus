package ru.sharipov.domain.atm.impl;

import ru.sharipov.domain.atm.BalanceCalculator;
import ru.sharipov.domain.banknote.Banknote;
import ru.sharipov.domain.banknote.BanknoteStorage;

import java.math.BigDecimal;
import java.util.List;

public class BalanceCalculatorImpl implements BalanceCalculator {
    private final BanknoteStorage banknoteStorage;

    public BalanceCalculatorImpl(BanknoteStorage banknoteStorage) {
        this.banknoteStorage = banknoteStorage;
    }

    @Override
    public BigDecimal calculateBalance() {
        List<Banknote> banknotes = banknoteStorage.getBanknotes();
        BigDecimal balance = banknotes.stream()
                .map(Banknote::getDenomination)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        banknoteStorage.addBanknotes(banknotes);
        return balance;
    }
}
