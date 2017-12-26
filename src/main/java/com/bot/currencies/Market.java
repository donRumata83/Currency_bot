package com.bot.currencies;

import com.bot.enums.MarketType;

public class Market {
    private float ask;
    private float bid;
    private MarketType marketType;

    public float getAsk() {
        return ask;
    }

    public float getBid() {
        return bid;
    }

    Market() {
        this.ask = 0.0f;
        this.bid = 0.0f;
    }

    public Market(float ask, float bid, MarketType marketType) {
        this.ask = ask;
        this.bid = bid;
        this.marketType = marketType;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    @Override
    public String toString() {
        return "Market{" +
                "ask=" + ask +
                ", bid=" + bid +
                '}';
    }
}
