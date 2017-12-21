package com.bot.currencies;

import com.bot.enums.City;
import com.bot.enums.MarketType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Currency implements Serializable {
    private String name;
    private String mark;
    private Date date = new Date();
    private Map<MarketType, Market> map;
    private Map<City, Market> auctionMap;

    public Currency(String name, String mark) {
        this.name = name;
        this.mark = mark;
        this.map = new HashMap<>();
        this.auctionMap = new HashMap<>();
        initAllmaps();
    }

    private void initAllmaps() {
        map.put(MarketType.NBU, new Market());
        map.put(MarketType.BANKS, new Market());
        map.put(MarketType.AUCTION, new Market());
        map.put(MarketType.MB_MARKET, new Market());
        for (City city: City.values()) {
            auctionMap.put(city, new Market());
        }
    }

    public float getNbu_ask() {
        return map.get(MarketType.NBU).getAsk();
    }

    public float getMb_ask() {
        return map.get(MarketType.MB_MARKET).getAsk();
    }

    public float getMb_bid() {
        return map.get(MarketType.MB_MARKET).getBid();
    }

    public float getBank_ask() {
        return map.get(MarketType.BANKS).getAsk();
    }

    public float getBank_bid() {
        return map.get(MarketType.BANKS).getBid();
    }

    public float getAuc_ask(City city) {
        return auctionMap.get(city).getAsk();
    }

    public float getAuc_bid(City city) {
        return auctionMap.get(city).getBid();
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMark() {
        return mark;
    }

    public void update(Market market) {
        MarketType type = market.getMarketType();
        map.put(type, market);
    }

    public void setAuctionMap(Map<City, Market> auctionMap) {
        this.auctionMap = auctionMap;
    }

    public void updateDate() {
        this.date = new Date();
    }


}
