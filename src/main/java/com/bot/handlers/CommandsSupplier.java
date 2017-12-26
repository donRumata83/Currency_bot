package com.bot.handlers;


import com.bot.enums.CalcCommands;
import com.bot.enums.Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandsSupplier {
    private static Map<String, Commands> map;
    private static Map<String, CalcCommands> calcMap;

    public void create() {
        map = new HashMap<>();
        map.put(KeyboardSupplier.usd, Commands.USD);
        map.put(KeyboardSupplier.eur, Commands.EURO);
        map.put(KeyboardSupplier.btc, Commands.BTC);
        map.put(KeyboardSupplier.rub, Commands.RUB);
        map.put(KeyboardSupplier.other, Commands.OTHER);
        map.put(KeyboardSupplier.calc, Commands.CALC);
        map.put("/donate", Commands.DONATE);
        map.put("/new", Commands.NEW);
        map.put("/start", Commands.START);
        map.put("/users", Commands.STAT);
        map.put("/help", Commands.HELP);
        map.put("/mstat", Commands.MSTAT);
        calcMap = new HashMap<>();
        calcMap.put(KeyboardSupplier.sellUsd, CalcCommands.SELL_USD);
        calcMap.put(KeyboardSupplier.buyUsd, CalcCommands.BUY_USD);
        calcMap.put(KeyboardSupplier.sellEur, CalcCommands.SELL_EUR);
        calcMap.put(KeyboardSupplier.buyEur, CalcCommands.BUY_EUR);
        calcMap.put(KeyboardSupplier.buyRub, CalcCommands.BUY_RUB);
        calcMap.put(KeyboardSupplier.sellRub, CalcCommands.SELL_RUB);
        calcMap.put(KeyboardSupplier.exit, CalcCommands.EXIT);
    }

    public static Commands getComand(String message) {
        return map.get(message);
    }

    public static CalcCommands getCalcCommand(String message) {
        return calcMap.getOrDefault(message, CalcCommands.NOT_NUMBER);
    }
}
