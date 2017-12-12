package com.bot.enums;

/**
 * Type of markets
 */
public enum MarketType {
    NBU("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json"),
    BANKS("http://api.minfin.com.ua/summary/"),
    AUCTION("http://api.minfin.com.ua/auction/info/"),
    MB_MARKET("http://api.minfin.com.ua/mb/"),
    BTC("https://api.cryptonator.com/api/ticker/btc-usd");

    private String request;

    MarketType(String request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return this.request;
    }
}
