package bot;

import bot.currencies.*;
import bot.enums.*;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class DataTransformerUtil {
    private CurrencyDB currency_DB;
    private Map<Commands, Currency> map = new HashMap<>();

    private final String format = "покупка %.3f , продажа %.3f";


    /**
     * Constructor
     *
     * @param currency_DB
     */
    DataTransformerUtil(CurrencyDB currency_DB) {
        this.currency_DB = currency_DB;
        this.getActualCurrencies();
    }

    /**
     * Actualize currency data from data base
     */
    private void getActualCurrencies() {
        this.map = currency_DB.getActualCurrencyStorage();

    }

    /**
     * Gets actual currency and transform into text for sending
     *
     * @param currency type
     * @return formatted text message for sending
     */
    private String getMessage(@NotNull Currency currency) {
        getActualCurrencies();
        return "*" + currency.getName() + "* " + getMark(currency) + " на "
                + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) +
                "\n" + "*Межбанк:* \n" +
                String.format(format, currency.getMb_ask(), currency.getMb_bid()) +
                "\n" + "*Средний курс в банках:* \n" +
                String.format(format, currency.getBank_ask(), currency.getBank_bid()) +
                "\n" + "*НБУ:* \n" +
                String.format(format, currency.getNbu_ask(), currency.getNbu_bid()) +
                "\n" + "*Аукцион:* \n" +
                String.format(format, currency.getAuc_ask(), currency.getAuc_bid());
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

    private String getMark(@NotNull Currency currency) {
        switch (currency.getName()) {
            case "Доллар США":
                return Mark.USD.getMark();
            case "Евро":
                return Mark.EUR.getMark();
            case "Десять Российских рублей":
                return Mark.RUB.getMark();
            default:
                return "";
        }
    }
}
