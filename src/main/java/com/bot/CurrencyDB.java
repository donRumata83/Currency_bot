package com.bot;

import com.bot.currencies.Currency;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;
import com.bot.updaters.Updater;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.validation.constraints.NotNull;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyDB {
    private HashMap<Commands, com.bot.currencies.Currency> actualCurrencyStorage;
    private Updater updater;
    private Updater bitCoinUpdater;

    private static final int TIMEOUT_5MIN = 1000 * 60 * 5;
    private static final int TIMEOUT_1HOUR = (1000 * 60 * 60) + 100;
    private static final int TIMEOUT_30SEC = 1000 * 30;


    CurrencyDB(Updater updater, Updater bitCoinUpdater) {
        /*try (InputStream reader = CurrencyBot.class.getResourceAsStream("/currency.properties")) {
            ObjectMapper maper = new ObjectMapper();
            Properties props = new Properties();
            props.load(reader);
            this.actualCurrencyStorage = new HashMap<>();
            this.actualCurrencyStorage.put(Commands.USD, maper.readValue(props.getProperty("usd"), com.bot.currencies.Currency.class));
            this.actualCurrencyStorage.put(Commands.EURO, maper.readValue(props.getProperty("eur"), com.bot.currencies.Currency.class));
            this.actualCurrencyStorage.put(Commands.RUB, maper.readValue(props.getProperty("rub"), com.bot.currencies.Currency.class));
            ;
        } catch (Exception e) {
            e.printStackTrace();*/
        this.actualCurrencyStorage = new HashMap<Commands, com.bot.currencies.Currency>() {{
            put(Commands.USD, new com.bot.currencies.Currency("Доллар США"));
            put(Commands.EURO, new com.bot.currencies.Currency("Евро"));
            put(Commands.RUB, new com.bot.currencies.Currency("Десять Российских рублей"));
            put(Commands.BTC, new Currency("Биткоин"));
        }};

        this.updater = updater;
        this.bitCoinUpdater = bitCoinUpdater;
        fiveMinuteUpdateTimer();
        thirtySecondsUpdateTimer();
        //saveTimer();
    }

    public HashMap<Commands, com.bot.currencies.Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
    }

    private void fiveMinuteUpdateTimer() {
        Thread run = new Thread(() -> {
            Deque<MarketType> request_queue = new ArrayDeque<>
                    (Arrays.asList(MarketType.MB_MARKET, MarketType.NBU, MarketType.BANKS, MarketType.AUCTION));
            while (true) {
                try {
                    updateBTC(bitCoinUpdater.sendRequest(MarketType.BTC));
                    MarketType request = request_queue.pollFirst();
                    List<Float> response = updater.sendRequest(request);
                    System.out.println(response);
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                    switch (request) {
                        case NBU:
                            updateNBU(response);
                            request_queue.addLast(request);
                            break;
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
            usd.setDate(new Date());
            actualCurrencyStorage.put(Commands.EURO, euro);

            com.bot.currencies.Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setMb_ask(response.get(4) * 10);
            rub.setMb_bid(response.get(5) * 10);
            usd.setDate(new Date());
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

    private void updateNBU(@NotNull List<Float> response) {
        if (response.size() != 0) {
            com.bot.currencies.Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setNbu_ask(response.get(0));
            usd.setNbu_bid(response.get(1));
            actualCurrencyStorage.put(Commands.USD, usd);

            com.bot.currencies.Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setNbu_ask(response.get(2));
            euro.setNbu_bid(response.get(3));
            actualCurrencyStorage.put(Commands.EURO, euro);

            Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setNbu_ask(response.get(4) * 10);
            rub.setNbu_bid(response.get(5) * 10);
            actualCurrencyStorage.put(Commands.RUB, rub);
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
        try (FileOutputStream fis = new FileOutputStream(CurrencyBot.class.getResource("/currency.properties").getFile(), false);) {
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
