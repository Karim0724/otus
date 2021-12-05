import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sharipov.domain.Atm;
import ru.sharipov.domain.banknote.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AtmTest {
    private final List<Banknote> defBanknotes = new ArrayList<>();

    private void addBanknotes(int count, Class<? extends Banknote> banknoteClass) {
        try {
            for (int i = 0; i < count; i++) {
                defBanknotes.add(banknoteClass.getDeclaredConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while creating banknotes: " + banknoteClass.getName(), e);
        }
    }

    @Test
    public void atmShouldReturnMoney() {
        addBanknotes(5, ThousandBanknote.class);
        addBanknotes(10, HundredBanknote.class);
        addBanknotes(10, TenBanknote.class);
        addBanknotes(100, OneBanknote.class);
        BigDecimal moneyToGet = new BigDecimal(1201);

        Atm atm = new Atm(defBanknotes);
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
        addBanknotes(5, ThousandBanknote.class);

        Atm atm = new Atm(defBanknotes);
        Assertions.assertThrows(RuntimeException.class, () -> {
           atm.getMoney(new BigDecimal(5001));
        });
    }

    @Test
    public void atmShoudAcceptBanknote() {
        addBanknotes(5, ThousandBanknote.class);

        Atm atm = new Atm(defBanknotes);
        atm.acceptBanknote(new ThousandBanknote());
        Assertions.assertEquals(new BigDecimal(6000), atm.showBalance());
    }
}
