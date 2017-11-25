import Currencies.*;
import Enums.Commands;

import java.util.HashMap;

public class Currency_DB {
    private HashMap<Commands, Currency> actualCurrencyStorage;

    public Currency_DB() {
        this.actualCurrencyStorage = new HashMap<>();
        actualCurrencyStorage.put(Commands.USD, new Currency("Доллар США"));
        actualCurrencyStorage.put(Commands.EURO, new Currency("Евро"));
        actualCurrencyStorage.put(Commands.RUB, new Currency("Российский рубль"));
        actualCurrencyStorage.put(Commands.GBP, new Currency("Британский фунт стерлингов"));
    }

    public HashMap<Commands, Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
    }
}
