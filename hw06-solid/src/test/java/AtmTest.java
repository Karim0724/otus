import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sharipov.domain.atm.Atm;
import ru.sharipov.domain.atm.BalanceCalculator;
import ru.sharipov.domain.atm.MoneyProvider;
import ru.sharipov.domain.atm.impl.BalanceCalculatorImpl;
import ru.sharipov.domain.atm.impl.MoneyProviderImpl;
import ru.sharipov.domain.atm.impl.SberAtm;
import ru.sharipov.domain.banknote.*;
import ru.sharipov.domain.banknote.impl.BanknoteStorageImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AtmTest {
    private final BanknoteStorageImpl storage = new BanknoteStorageImpl();
    private final MoneyProvider moneyProvider = new MoneyProviderImpl(storage);
    private final BalanceCalculator balanceCalculator = new BalanceCalculatorImpl(storage);
    private final Atm atm = new SberAtm(moneyProvider, balanceCalculator, storage);

    private List<Banknote> cloneBanknotes(int count, Banknote banknote) {
        List<Banknote> clones = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            clones.add(new Banknote(banknote.getDenomination()));
        }
        return clones;
    }

    @Test
    public void atmShouldReturnMoney() {
        List<Banknote> banknotes = new ArrayList<>();
        banknotes.addAll(cloneBanknotes(5, new Banknote(1000)));
        banknotes.addAll(cloneBanknotes(10, new Banknote(10)));
        banknotes.addAll(cloneBanknotes(10, new Banknote(10)));
        banknotes.addAll(cloneBanknotes(100, new Banknote(1)));
        BigDecimal moneyToGet = new BigDecimal(1201);

        atm.acceptBanknotes(banknotes);
        System.out.println("Balance: " + atm.showBalance());
        List<Banknote> returnedBanknotes = atm.getMoney(moneyToGet);
        System.out.println("Get Money: " + returnedBanknotes);
        System.out.println("Balance after withdraw: " + atm.showBalance());
        Assertions.assertEquals(moneyToGet,
                returnedBanknotes.stream()
                .map(Banknote::getDenomination)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Test
    public void atmShouldThrowExceptionIfNotEnoughMoney() {
        List<Banknote> banknotes = new ArrayList<>(cloneBanknotes(5, new Banknote(1000)));

        atm.acceptBanknotes(banknotes);
        Assertions.assertThrows(RuntimeException.class, () -> {
           atm.getMoney(new BigDecimal(5001));
        });
    }

    @Test
    public void atmShouldAcceptBanknote() {
        Banknote banknote = new Banknote(1000);

        atm.acceptBanknote(banknote);
        Assertions.assertEquals(new BigDecimal(1000), atm.showBalance());
    }
}
