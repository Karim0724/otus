package ru.sharipov.domain;

import ru.sharipov.domain.banknote.Banknote;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Atm {
    private List<Banknote> banknotes;

    public Atm(List<Banknote> banknotes) {
        this.banknotes = banknotes;
    }

    public void acceptBanknote(Banknote banknote) {
        banknotes.add(banknote);
    }

    public List<Banknote> getMoney(BigDecimal moneyToGet) {
        List<Banknote> banknotesToReturn = new ArrayList<>();
        List<Banknote> sorted = banknotes.stream()
                .sorted(Comparator.comparing(Banknote::getDenomination).reversed())
                .collect(Collectors.toList());
        Iterator<Banknote> banknoteIterator = sorted.iterator();
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
            throw new RuntimeException("Can't get money");
        } else {
            banknotes = sorted;
            return banknotesToReturn;
        }
    }

    public BigDecimal showBalance() {
        return banknotes.stream()
                .map(Banknote::getDenomination)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
