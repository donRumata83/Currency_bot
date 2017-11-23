package Enums;

public enum Commands {
    USD,
    EURO,
    GBP,
    RUB,
    NOT_CURRENCY,
    START,
    HELP;

    public static Commands convert(String text) {
        switch (text) {
            case "/START": return START;
            case "/USD":
                return USD;
            case "/EURO":
                return EURO;
            case "/GBP":
                return GBP;
            case "/RUB":
                return RUB;
            case "/HELP": return HELP;
            default:
                return NOT_CURRENCY;
        }
    }
}
