package Bot;

import Bot.Currencies.*;
import Bot.Enums.*;
import java.util.HashMap;
import java.util.Map;

class Data_transformer_Util {
    private Currency_DB currency_DB;
    private Map<Commands, Currency> map = new HashMap<>();

    private final String format = "покупка %.2f , продажа %.2f";


    /**
     * Constructor
     * @param currency_DB
     */
    Data_transformer_Util(Currency_DB currency_DB) {
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
     * @param currency type
     * @return formatted text message for sending
     */
    private String getMessage(Currency currency) {
        getActualCurrencies();
        String stringBuilder = "*" +currency.getName() + "*"+  currency.getMark() +
                "\n" + "*Межбанк:* \n" +
                String.format(format, currency.getMb_ask(), currency.getMb_bid()) +
                "\n" + "*Средний курс в банках:* \n" +
                String.format(format, currency.getBank_ask(), currency.getBank_bid()) +
                "\n" + "*НБУ:* \n" +
                String.format(format, currency.getNbu_ask(), currency.getNbu_bid()) +
                "\n" + "*Аукцион:* \n" +
                String.format(format, currency.getAuc_ask(), currency.getAuc_bid());
        return stringBuilder;
    }

    /**
     * Return local USD value
     * @return text USD value
     */
    String getUSD() {
        return getMessage(map.get(Commands.USD));
    }

    /**
     * Return local EURO value
     * @return text EURO value
     */
    String getEuro() {
        return getMessage(map.get(Commands.EURO));
    }

    /**
     * Return local RUB value
     * @return text RUB value
     */
    String getRub(){return getMessage(map.get(Commands.RUB));
    }

}