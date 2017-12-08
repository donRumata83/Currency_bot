package com.bot.enums;

/**
 * Enum for typical commands for bot
 */
public enum Commands {
    USD, EURO, RUB,
    NOT_CURRENCY, START, HELP,
    STAT, MSTAT, SEARCH,
    BTC;

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
            case "DOLLAR":
            case "DOLAR":
                return USD;
            case "/EURO":
            case "EURO":
            case "EUR":
                return EURO;
            case "/RUB":
            case "RUB":
            case "RUR":
            case "RUBLE":
                return RUB;
            case "BTC":
            case "BITCOIN":
                return BTC;
            case "/HELP":
            case "HELP":
                return HELP;
            case "STAT":
                return STAT;
            case "MSTAT":
                return MSTAT;
            case "/SEARCH":
                return SEARCH;
            default:
                return NOT_CURRENCY;
        }
    }
}
