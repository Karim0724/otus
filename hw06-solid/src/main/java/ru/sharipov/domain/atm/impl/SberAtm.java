package ru.sharipov.domain.atm.impl;

import ru.sharipov.domain.atm.Atm;
import ru.sharipov.domain.atm.BalanceCalculator;
import ru.sharipov.domain.atm.BanknoteAcceptor;
import ru.sharipov.domain.atm.MoneyProvider;
import ru.sharipov.domain.banknote.Banknote;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class SberAtm implements Atm {
    private final MoneyProvider moneyProvider;
    private final BalanceCalculator balanceCalculator;
    private final BanknoteAcceptor banknoteAcceptor;

    public SberAtm(MoneyProvider moneyProvider, BalanceCalculator balanceCalculator, BanknoteAcceptor banknoteAcceptor) {
        this.moneyProvider = moneyProvider;
        this.balanceCalculator = balanceCalculator;
        this.banknoteAcceptor = banknoteAcceptor;
    }

    @Override
    public void acceptBanknote(Banknote banknote) {
        banknoteAcceptor.accept(banknote);
    }

    @Override
    public void acceptBanknotes(Collection<Banknote> banknotes) {
        banknoteAcceptor.accept(banknotes);
    }

    @Override
    public BigDecimal showBalance() {
        return balanceCalculator.calculateBalance();
    }

    @Override
    public List<Banknote> getMoney(BigDecimal moneyToGet) {
        return moneyProvider.getMoney(moneyToGet);
    }
}
