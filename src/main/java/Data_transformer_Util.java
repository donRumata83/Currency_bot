import Currencies.*;

class Data_transformer_Util {
    private Currency_DB currency_DB;

    private USD usd;
    private EURO euro;
    private RUB rub;
    private GBP gbp;

    private final String format = "покупка %.3f , продажа %.3f";
    private final String info = "Информация предоставлена с www.minfin.com.ua/currency/";

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
        this.usd = currency_DB.getUSD();
        this.euro = currency_DB.getEuro();
        this.rub = currency_DB.getRub();
        this.gbp = currency_DB.getGbp();

    }

    /**
     * Gets actual currency and transform into text for sending
     * @param currency type
     * @return formatted text message for sending
     */
    private String getMessage(Currency currency) {
        getActualCurrencies();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(currency.getName());
        stringBuilder.append("\n").append("Межбанк: \n")
                .append(String.format(format,currency.getMb_ask(),currency.getMb_bid()));
        stringBuilder.append("\n").append("Средний курс в банках: \n")
                .append(String.format(format,currency.getBank_ask(),currency.getBank_bid()));
        stringBuilder.append("\n").append("НБУ: \n")
                .append(String.format(format,currency.getNbu_ask(),currency.getNbu_bid()));
        stringBuilder.append("\n").append("Аукцион: \n").
                append(String.format(format,currency.getAuc_ask(),currency.getAuc_bid()));
        stringBuilder.append("\n").append(info);
        return stringBuilder.toString();
    }

    /**
     * Return local USD value
     * @return text USD value
     */
    String getUSD() {
        return getMessage(usd);
    }

    /**
     * Return local EURO value
     * @return text EURO value
     */
    String getEuro() {
        return getMessage(euro);
    }

    /**
     * Return local RUB value
     * @return text RUB value
     */
    String getRub() {
        return getMessage(rub);
    }

    /**
     * Return local GBP value
     * @return text GBP value
     */
    String getGbp() {
        return getMessage(gbp);
    }
}
