package ru.sharipov.domain.banknote;

import ru.sharipov.domain.banknote.Banknote;

import java.util.Collection;
import java.util.List;

public interface BanknoteStorage {
    void addBanknote(Banknote banknote);
    void addBanknotes(Collection<Banknote> banknotes);
    List<Banknote> getBanknotes();
}
