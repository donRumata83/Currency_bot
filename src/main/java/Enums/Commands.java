package Enums;

public enum Commands {
    USD,
    EURO,
    GBP,
    RUB,
    NOT_CURRENCY,
    START,
    HELP,
    STAT;

    public static Commands convert(String text) {
        switch (text) {
            case "/START":
                return START;
            case "/USD":
            case "USD":
            case "ДОЛЛАР":
            case "ДОЛАР":
                return USD;
            case "/EURO":
            case "EURO":
            case "ЕВРО":
                return EURO;
            case "/GBP":
            case "POUND":
            case "ФУНТ":
                return GBP;
            case "/RUB":
            case "RUBLE":
            case "РУБЛЬ":
                return RUB;
            case "/HELP":
            case "HELP":
                return HELP;
            case "STAT":return STAT;
            default:
                return NOT_CURRENCY;
        }
    }
}
