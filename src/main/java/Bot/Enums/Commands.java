package Bot.Enums;

/**
 * Enum for typical commands for bot
 */
public enum Commands {
    USD, EURO, RUB,
    NOT_CURRENCY, START, HELP,
    STAT, MSTAT;

    /**
     * Converts text from user chat to enum
     *
     * @param text from user message
     * @return enum
     */
    public static Commands convert(String text) {
        switch (text) {
            case "/START":
                return START;
            case "/USD":
            case "USD":
            case "ДОЛЛАР":
            case "ДОЛАР":
            case "DOLLAR":
            case "DOLAR":
                return USD;
            case "/EURO":
            case "EURO":
            case "ЕВРО":
                return EURO;
            case "/RUB":
            case "RUBLE":
            case "РУБЛЬ":
                return RUB;
            case "/HELP":
            case "HELP":
            case "ПОМОЩЬ":
                return HELP;
            case "STAT":
                return STAT;
            case "MSTAT":
                return MSTAT;
            default:
                return NOT_CURRENCY;
        }
    }
}
