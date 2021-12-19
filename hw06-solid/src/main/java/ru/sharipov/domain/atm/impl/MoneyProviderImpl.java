package ru.sharipov.domain.atm.impl;

import ru.sharipov.domain.atm.MoneyProvider;
import ru.sharipov.domain.banknote.Banknote;
import ru.sharipov.domain.banknote.BanknoteStorage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MoneyProviderImpl implements MoneyProvider {
    private final BanknoteStorage banknoteStorage;

    public MoneyProviderImpl(BanknoteStorage banknoteStorage) {
        this.banknoteStorage = banknoteStorage;
    }

    @Override
    public List<Banknote> getMoney(BigDecimal moneyToGet) {
        List<Banknote> banknotes = banknoteStorage.getBanknotes();
        List<Banknote> banknotesToReturn = new ArrayList<>();
        List<Banknote> sortedByDenomination = banknotes.stream()
                .sorted(Comparator.comparing(Banknote::getDenomination).reversed())
                .collect(Collectors.toList());
        Iterator<Banknote> banknoteIterator = sortedByDenomination.iterator();
        BigDecimal currentMoney = new BigDecimal(moneyToGet.toString());
        while (banknoteIterator.hasNext()) {
            Banknote nextBankNote = banknoteIterator.next();
            BigDecimal difference = currentMoney.subtract(nextBankNote.getDenomination());
            if (difference.compareTo(BigDecimal.ZERO) >= 0) {
                banknoteIterator.remove();
                banknotesToReturn.add(nextBankNote);
                currentMoney = difference;
            }
        }
        if (currentMoney.compareTo(BigDecimal.ZERO) != 0) {
            banknoteStorage.addBanknotes(banknotes);
            throw new RuntimeException("Can't get money");
        } else {
            banknoteStorage.addBanknotes(sortedByDenomination);
            return banknotesToReturn;
        }
    }
}
