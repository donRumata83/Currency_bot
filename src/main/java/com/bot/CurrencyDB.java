package com.bot;

import com.bot.currencies.Currency;
import com.bot.currencies.SimpleCurrency;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;
import com.bot.updaters.NBUUpdater;
import com.bot.updaters.Updater;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyDB {
    private HashMap<Commands, com.bot.currencies.Currency> actualCurrencyStorage;
    private List<SimpleCurrency> simpleCurrencyList;
    private Updater updater;
    private Updater bitCoinUpdater;
    private NBUUpdater nbuUpdater;

    private static final int TIMEOUT_5MIN = 1000 * 60 * 5;
    private static final int TIMEOUT_30SEC = 1000 * 30;

    private static String usd;
    private static String eur;
    private static String rub;
    private static String bc;


    CurrencyDB(Updater updater, Updater bitCoinUpdater, NBUUpdater nbu) {
        loadProperties();
        this.actualCurrencyStorage = new HashMap<Commands, com.bot.currencies.Currency>() {{
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

    public HashMap<Commands, Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
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
                    MarketType request = request_queue.pollFirst();
                    List<Float> response = updater.sendRequest(request);
                    System.out.println(response);
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                    switch (request) {
                        case AUCTION:
                            updateAUC(response);
                            request_queue.addLast(request);
                            break;
                        case BANKS:
                            updateBank(response);
                            request_queue.addLast(request);
                            break;
                        case MB_MARKET:
                            updateMB(response);
                            request_queue.addLast(request);
                            break;
                    }
                    Thread.sleep(TIMEOUT_5MIN);
                } catch (InterruptedException ex) {
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
                    Thread.sleep(TIMEOUT_30SEC);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        run.start();
    }

    private void updateMB(@NotNull List<Float> response) {
        if (response.size() != 0) {
            com.bot.currencies.Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setMb_ask(response.get(0));
            usd.setMb_bid(response.get(1));
            usd.setDate(new Date());
            actualCurrencyStorage.put(Commands.USD, usd);

            com.bot.currencies.Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setMb_ask(response.get(2));
            euro.setMb_bid(response.get(3));
            euro.setDate(new Date());
            actualCurrencyStorage.put(Commands.EURO, euro);

            com.bot.currencies.Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setMb_ask(response.get(4) * 10);
            rub.setMb_bid(response.get(5) * 10);
            euro.setDate(new Date());
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateBank(@NotNull List<Float> response) {
        if (response.size() != 0) {
            com.bot.currencies.Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setBank_ask(response.get(0));
            usd.setBank_bid(response.get(1));
            actualCurrencyStorage.put(Commands.USD, usd);

            com.bot.currencies.Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setBank_ask(response.get(2));
            euro.setBank_bid(response.get(3));
            actualCurrencyStorage.put(Commands.EURO, euro);

            com.bot.currencies.Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setBank_ask(response.get(4) * 10);
            rub.setBank_bid(response.get(5) * 10);
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateAUC(@NotNull List<Float> response) {
        if (response.size() != 0) {
            com.bot.currencies.Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setAuc_ask(response.get(0));
            usd.setAuc_bid(response.get(1));
            usd.setDate(new Date());
            actualCurrencyStorage.put(Commands.USD, usd);

            com.bot.currencies.Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setAuc_ask(response.get(2));
            euro.setAuc_bid(response.get(3));
            euro.setDate(new Date());
            actualCurrencyStorage.put(Commands.EURO, euro);

            com.bot.currencies.Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setAuc_ask(response.get(4) * 10);
            rub.setAuc_bid(response.get(5) * 10);
            rub.setDate(new Date());
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateNBU(@NotNull List<SimpleCurrency> response) {
        for (SimpleCurrency currency: response) {
            if (currency.getMark().equals("USD") || currency.getMark().equals("EUR") || currency.getMark().equals("RUB"))
            {
                if (currency.getMark().equals("USD")) {
                    Currency usd = actualCurrencyStorage.get(Commands.USD);
                    usd.setNbu_ask(currency.getRate());
                    usd.setNbu_bid(currency.getRate());
                    actualCurrencyStorage.put(Commands.USD, usd);
                }
                if (currency.getMark().equals("EUR")) {
                    Currency eur = actualCurrencyStorage.get(Commands.EURO);
                    eur.setNbu_ask(currency.getRate());
                    eur.setNbu_bid(currency.getRate());
                    actualCurrencyStorage.put(Commands.EURO, eur);
                }
                if (currency.getMark().equals("RUB")) {
                    Currency rub = actualCurrencyStorage.get(Commands.RUB);
                    rub.setNbu_ask(currency.getRate());
                    rub.setNbu_bid(currency.getRate());
                    actualCurrencyStorage.put(Commands.RUB, rub);
                }
            } else {
                simpleCurrencyList.add(currency);
            }
        }
    }

    private void updateBTC(List<Float> response) {
        if (response.size() != 0) {
            Currency btc = actualCurrencyStorage.get(Commands.BTC);
            btc.setAuc_ask(response.get(0));
            btc.setDate(new Date());
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
