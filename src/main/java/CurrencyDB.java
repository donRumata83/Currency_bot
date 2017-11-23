import Currencies.*;





class CurrencyDB {
    private Currency_Updater currency_updater;

    private USD usd;
    private EURO euro;
    private RUB rub;
    private GBP gbp;

    CurrencyDB(Currency_Updater currency_updater) {
        this.currency_updater = currency_updater;
        this.getActualCurrencies();
    }

    private void getActualCurrencies() {
        this.usd = currency_updater.getUSD();
        this.euro = currency_updater.getEuro();
        this.rub = currency_updater.getRub();
        this.gbp = currency_updater.getGbp();

    }

    private String getMessage(Currency currency) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder(currency.getName());
        stringBuilder.append("/n").append(String.format("Межбанк: покупка %f , продажа %f ",currency.getMb_ask(),currency.getMb_bid()));
        stringBuilder.append("/n").append(String.format("Средний курс в банках: покупка %f , продажа %f ",currency.getBank_ask(),currency.getBank_bid()));
        stringBuilder.append("/n").append(String.format("НБУ: покупка %f , продажа %f ",currency.getNbu_ask(),currency.getNbu_bid()));
        stringBuilder.append("/n").append(String.format("Аукцион: покупка %f , продажа %f ",currency.getAuc_ask(),currency.getAuc_bid()));
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
