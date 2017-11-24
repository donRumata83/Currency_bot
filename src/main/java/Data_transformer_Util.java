import Currencies.*;

class Data_transformer_Util {
    private Currency_DB currency_DB;

    private USD usd;
    private EURO euro;
    private RUB rub;
    private GBP gbp;

    private String format = "покупка %.3f , продажа %.3f";

    Data_transformer_Util(Currency_DB currency_DB) {
        this.currency_DB = currency_DB;
        this.getActualCurrencies();
    }

    private void getActualCurrencies() {
        this.usd = currency_DB.getUSD();
        this.euro = currency_DB.getEuro();
        this.rub = currency_DB.getRub();
        this.gbp = currency_DB.getGbp();

    }

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
        return stringBuilder.toString();
    }

    String getUSD() {
        return getMessage(usd);
    }

    String getEuro() {
        return getMessage(euro);
    }

    String getRub() {
        return getMessage(rub);
    }

    String getGbp() {
        return getMessage(gbp);
    }
}
