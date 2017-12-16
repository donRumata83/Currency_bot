package com.bot;

import com.bot.currencies.Currency;
import com.bot.currencies.Market;
import com.bot.currencies.SimpleCurrency;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;
import com.bot.updaters.NBUUpdater;
import com.bot.updaters.Updater;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyDB {
    private ConcurrentHashMap<Commands, Currency> actualCurrencyStorage;
    private List<SimpleCurrency> simpleCurrencyList;
    private Updater updater;
    private Updater bitCoinUpdater;
    private NBUUpdater nbuUpdater;

    private static final int TIMEOUT_5MIN = 1000 * 60 * 5;
    private static final int TIMEOUT_1MIN = 1000 * 60;

    private static String usd;
    private static String eur;
    private static String rub;
    private static String bc;


    CurrencyDB(Updater updater, Updater bitCoinUpdater, NBUUpdater nbu) {
        loadProperties();
        this.actualCurrencyStorage = new ConcurrentHashMap<>() {{
            put(Commands.USD, new Currency(usd, "usd"));
            put(Commands.EURO, new Currency(eur, "eur"));
            put(Commands.RUB, new Currency(rub, "rub"));
            put(Commands.BTC, new Currency(bc, "btc"));
        }};
        this.updater = updater;
        this.bitCoinUpdater = bitCoinUpdater;
        this.nbuUpdater = nbu;
        this.simpleCurrencyList = new ArrayList<>();
        fiveMinuteUpdateTimer();
        thirtySecondsUpdateTimer();
    }

    private void loadProperties() {
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream("/message.properties");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
            props.load(reader);
            usd = props.getProperty("usd");
            eur = props.getProperty("eur");
            rub = props.getProperty("rub");
            bc = props.getProperty("btc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Commands, Currency> getActualCurrencyStorage() {
        return this.actualCurrencyStorage;
    }

    public List<SimpleCurrency> getSimpleCurrencyList() {
        return simpleCurrencyList;
    }

    private void fiveMinuteUpdateTimer() {
        Thread run = new Thread(() -> {
            Deque<MarketType> request_queue = new ArrayDeque<>
                    (Arrays.asList(MarketType.MB_MARKET, MarketType.BANKS, MarketType.AUCTION));
            while (true) {
                try {
                    updateNBU(nbuUpdater.sendRequest(MarketType.NBU));
                    Thread.sleep(1000);
                    MarketType request = request_queue.pollFirst();
                    Map<Commands, Market> response = updater.sendRequest(request);
                    updateMainCurrency(response);
                    request_queue.addLast(request);
                    Thread.sleep(TIMEOUT_5MIN);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        run.start();
    }

    private void thirtySecondsUpdateTimer() {
        Thread run = new Thread(() -> {
            try {
                while (true) {
                    updateBTC(bitCoinUpdater.sendRequest(MarketType.BTC));
                    Thread.sleep(TIMEOUT_1MIN);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        run.start();
    }

    private void updateNBU(@NotNull List<SimpleCurrency> response) {
        List<SimpleCurrency> tmp = new ArrayList<>();
        for (SimpleCurrency currency : response) {
            if (currency.getMark().equals("USD") || currency.getMark().equals("EUR") || currency.getMark().equals("RUB")) {
                if (currency.getMark().equals("USD")) {
                    updateNBUone(Commands.USD, currency.getRate());
                }
                if (currency.getMark().equals("EUR")) {
                    updateNBUone(Commands.EURO, currency.getRate());
                }
                if (currency.getMark().equals("RUB")) {
                    updateNBUone(Commands.RUB, currency.getRate());
                }
            } else {
                tmp.add(currency);
            }
        }
        simpleCurrencyList = tmp;
    }

    private void updateNBUone(Commands currency, float rate) {
        Currency currency1 = actualCurrencyStorage.get(currency);
        currency1.update(new Market(rate, 0.0f, MarketType.NBU));
        actualCurrencyStorage.put(currency, currency1);
    }

    private void updateMainCurrency(Map<Commands, Market> response) {
        setNewDate();
        actualCurrencyStorage.get(Commands.USD).update(response.get(Commands.USD));
        actualCurrencyStorage.get(Commands.EURO).update(response.get(Commands.EURO));
        actualCurrencyStorage.get(Commands.RUB).update(response.get(Commands.RUB));
    }

    private void setNewDate() {
        for (Commands c : actualCurrencyStorage.keySet()) {
            actualCurrencyStorage.get(c).updateDate();
        }
    }

    private void updateBTC(Map<Commands, Market> response) {
        if (response.size() != 0) {
            Currency btc = actualCurrencyStorage.get(Commands.BTC);
            btc.setDate(new Date());
            btc.update(response.get(Commands.BTC));
            actualCurrencyStorage.put(Commands.BTC, btc);
        }
    }

    private void saveTimer() {
        new Thread(() -> {

            try {
                while (true) {
                    Thread.sleep(1000);
                    saveInFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void saveInFile() {
        try (FileOutputStream fis = new FileOutputStream(CurrencyBot.class.getResource("/currency.properties").getFile(), false)) {
            ObjectMapper mapper = new ObjectMapper();
            Properties props = new Properties();
            StringWriter stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, actualCurrencyStorage.get(Commands.USD));
            props.setProperty("usd", stringWriter.toString());
            stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, actualCurrencyStorage.get(Commands.EURO));
            props.setProperty("eur", stringWriter.toString());
            stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, actualCurrencyStorage.get(Commands.RUB));
            props.setProperty("rub", stringWriter.toString());
            props.store(fis, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
