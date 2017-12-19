package com.bot.updaters;

import com.bot.currencies.Market;
import com.bot.enums.City;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class AuctionUpdater {
    private final String usdUrl = "https://minfin.com.ua/currency/auction/usd/buy/";
    private final String eurUrl = "https://minfin.com.ua/currency/auction/eur/buy/";
    private final String rubUrl = "https://minfin.com.ua/currency/auction/rub/buy/";
    private List<City> cities = new ArrayList<>(Arrays.asList(City.values()));

    public Map<City, Market> update(Commands currency) {
        Map<City, Market> result = new HashMap<>();
        for (City city : cities) {
            try {
                result.put(city, getMarket(getFullUrl(currency, city)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private String getFullUrl(Commands currency, City city) {
        switch (currency) {
            case USD:
                return usdUrl + city;
            case EURO:
                return eurUrl + city;
            case RUB:
                return rubUrl + city;
            default:
                return "";
        }

    }

    private float getAsk(Document html) {
        return getFloatFromString(html.body().getElementsByClass("au-mid-buysell").first().text());
    }

    private float getBid(Document html) {
        return getFloatFromString(html.body().getElementsByClass("au-mid-buysell").last().text());    }

    private Market getMarket(String url) throws IOException {
        Document html = Jsoup.connect(url).get();
        return new Market(getAsk(html), getBid(html), MarketType.AUCTION);
    }

    private float getFloatFromString(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i))) {
                string.replace(string.charAt(i),  )
            }
        }
    }
}
