package ru.sharipov.domain.banknote;

public abstract class AbstractBanknote implements Banknote {
    @Override
    public String toString() {
        return "Banknote(" + getDenomination() + ")";
    }
}
