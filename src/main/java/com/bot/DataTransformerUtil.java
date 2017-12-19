package com.bot;

import com.bot.currencies.Currency;
import com.bot.currencies.SimpleCurrency;
import com.bot.enums.City;
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

public class DataTransformerUtil {
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
    private String getMessage(@NotNull Currency currency, City city) {
        getActualCurrencies();
        switch (currency.getName()) {
            case "Биткоин":
                return getBTC();
            default:
                return String.format(HEAD_FORMAT, currency.getName(), getMark(currency), new SimpleDateFormat("HH:mm dd.MM.yyyy").format(currency.getDate()),
                        NBU, currency.getNbu_ask(),
                        MB, currency.getMb_ask(), currency.getMb_bid(),
                        BANKS, currency.getBank_ask(), currency.getBank_bid(),
                        AUC, currency.getAuc_ask(city), currency.getAuc_bid(city));
        }
    }

    /**
     * Return local USD value
     *
     * @return text USD value
     */
    public String getUSD(City city) {
        return getMessage(map.get(Commands.USD), city);
    }

    /**
     * Return local EURO value
     *
     * @return text EURO value
     */
    public String getEuro(City city) {
        return getMessage(map.get(Commands.EURO), city);
    }

    /**
     * Return local RUB value
     *
     * @return text RUB value
     */
    public String getRub(City city) {
        return getMessage(map.get(Commands.RUB),city);
    }

    public String getBTC() {
        Currency btc = map.get(Commands.BTC);
        return String.format(BITCOIN, btc.getName(),
                new SimpleDateFormat("HH:mm dd.MM.yyyy").format(btc.getDate()),
                btc.getBank_ask());

    }

    public String getOtherCurrencyFirstHalf() {
        return (getOtherCurrency(otherCurrency.size() / 2, 0));
    }

    public String getOtherCurrencySecondHalf() {
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
