package ru.sharipov.domain.atm;

import ru.sharipov.domain.banknote.Banknote;

import java.util.Collection;

public interface BanknoteAcceptor {
    void accept(Banknote banknote);
    void accept(Collection<Banknote> banknotes);
}
