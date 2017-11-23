import Currencies.*;
import com.sun.javafx.binding.StringFormatter;


public class CurrencyDB {
    private Currency_Updater currency_updater;

    private USD usd;
    private EURO euro;
    private RUB rub;
    private GBP gbp;

    public CurrencyDB(Currency_Updater currency_updater) {
        this.currency_updater = currency_updater;
        this.getActualCurrencies();
    }

    private void getActualCurrencies() {
        this.usd = currency_updater.getUSD();
        this.euro = currency_updater.getEuro();
        this.rub = currency_updater.getRub();
        this.gbp = currency_updater.getGbp();

    }

    private String getMessage (Currency currency) {
        return "";
    }

    public String getUSD() {
        return getMessage(usd);
    }

    public String getEuro() {
        return getMessage(euro);
    }

    public String getRub() {
        return getMessage(rub);
    }

    public String getGbp() {
        return getMessage(gbp);
    }
}
