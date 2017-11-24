import Currencies.EURO;
import Currencies.GBP;
import Currencies.RUB;
import Currencies.USD;

public class Currency_DB {
    private USD usd = new USD();
    private EURO euro = new EURO();
    private RUB rub = new RUB();
    private GBP gbp = new GBP();

    public void getActualCurrencies() {

    }

    USD getUSD() {
        return this.usd;
    }

    EURO getEuro() {
        return this.euro;
    }

    RUB getRub() {
        return this.rub;
    }

    GBP getGbp() {
        return this.gbp;
    }


}
