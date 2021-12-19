package ru.sharipov.domain.banknote.impl;

import ru.sharipov.domain.atm.BanknoteAcceptor;
import ru.sharipov.domain.banknote.Banknote;
import ru.sharipov.domain.banknote.BanknoteStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BanknoteStorageImpl implements BanknoteStorage, BanknoteAcceptor {
    private final List<Banknote> banknotes = new ArrayList<>();

    @Override
    public void accept(Banknote banknote) {
        addBanknote(banknote);
    }

    @Override
    public void accept(Collection<Banknote> banknotes) {
        addBanknotes(banknotes);
    }

    @Override
    public void addBanknote(Banknote banknote) {
        this.banknotes.add(banknote);
    }

    @Override
    public void addBanknotes(Collection<Banknote> banknotes) {
        this.banknotes.addAll(banknotes);
    }

    @Override
    public List<Banknote> getBanknotes() {
        List<Banknote> toReturn = new ArrayList<>(banknotes);
        banknotes.clear();
        return toReturn;
    }

}
