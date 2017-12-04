package Bot.Enums;

/**
 * Type of markets
 */
public enum MarketType {
    NBU("http://api.minfin.com.ua/nbu/"),
    BANKS("http://api.minfin.com.ua/summary/"),
    AUCTION("http://api.minfin.com.ua/auction/info/"),
    MB_MARKET("http://api.minfin.com.ua/mb/");

    private String request;

    MarketType(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return this.request;
    }
}
