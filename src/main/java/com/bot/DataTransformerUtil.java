package com.bot;


import com.bot.currencies.Currency;
import com.bot.currencies.SimpleCurrency;
import com.bot.enums.Commands;
import com.bot.enums.Mark;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

class DataTransformerUtil {
    private CurrencyDB currency_DB;
    private Map<Commands, Currency> map = new HashMap<>();
    private List<SimpleCurrency> otherCurrency = new ArrayList<>();

    private static String HEAD_FORMAT;
    private static String BITCOIN;
    private static String MB;
    private static String BANKS;
    private static String NBU;
    private static String AUC;

    /**
     * Constructor
     *
     * @param currency_DB
     */
    DataTransformerUtil(CurrencyDB currency_DB) {
        this.currency_DB = currency_DB;
        this.getActualCurrencies();
        loadProperties();
    }

    /**
     * Actualize currency data from data base
     */
    private void getActualCurrencies() {
        this.map = currency_DB.getActualCurrencyStorage();
        this.otherCurrency = currency_DB.getSimpleCurrencyList();

    }

    /**
     * Gets actual currency and transform into text for sending
     *
     * @param currency type
     * @return formatted text message for sending
     */
    private String getMessage(@NotNull Currency currency) {
        getActualCurrencies();
        switch (currency.getName()) {
            case "Биткоин":
                return getBTC();
            default:
                return String.format(HEAD_FORMAT, currency.getName(), getMark(currency), new SimpleDateFormat("HH:mm dd.MM.yyyy").format(currency.getDate()),
                        NBU, currency.getNbu_ask(),
                        MB, currency.getMb_ask(), currency.getMb_bid(),
                        BANKS, currency.getBank_ask(), currency.getBank_bid(),
                        AUC, currency.getAuc_ask(), currency.getAuc_bid());
        }
    }

    /**
     * Return local USD value
     *
     * @return text USD value
     */
    String getUSD() {
        return getMessage(map.get(Commands.USD));
    }

    /**
     * Return local EURO value
     *
     * @return text EURO value
     */
    String getEuro() {
        return getMessage(map.get(Commands.EURO));
    }

    /**
     * Return local RUB value
     *
     * @return text RUB value
     */
    String getRub() {
        return getMessage(map.get(Commands.RUB));
    }

    String getBTC() {
        Currency btc = map.get(Commands.BTC);
        return String.format(BITCOIN, btc.getName(),
                new SimpleDateFormat("HH:mm dd.MM.yyyy").format(btc.getDate()),
                btc.getAuc_ask());

    }

    String getOtherCurrencyFirstHalf() {
        return getOtherCurrency(otherCurrency.size() / 2, 0);
    }

    String getOtherCurrencySecondHalf() {
        return getOtherCurrency(otherCurrency.size() / 2, otherCurrency.size() / 2);
    }

    private String getOtherCurrency(int limitValues, int skipValues) {
        return otherCurrency.stream().sorted().skip(skipValues).limit(limitValues).map(SimpleCurrency::toString).collect(Collectors.joining("\n"));
    }

    private String getMark(@NotNull Currency currency) {
        switch (currency.getMark()) {
            case "usd":
                return Mark.USD.getMark();
            case "eur":
                return Mark.EUR.getMark();
            case "rub":
                return Mark.RUB.getMark();
            case "btc":
                return Mark.BTC.getMark();
            default:
                return "";
        }
    }

    public Map<Commands, Currency> getMap() {
        return map;
    }

    private void loadProperties() {
        Properties props = new Properties();
        InputStream in = getClass().getResourceAsStream("/message.properties");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
            props.load(reader);
            HEAD_FORMAT = props.getProperty("head");
            BITCOIN = props.getProperty("bitcoin");
            MB = props.getProperty("mb");
            BANKS = props.getProperty("banks");
            NBU = props.getProperty("nbu");
            AUC = props.getProperty("auc");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
