package com.bot.updaters;

import com.bot.CurrencyBot;
import com.bot.currencies.Market;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;

import com.bot.updaters.Updater;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MinfinUpdater implements Updater {
    private String minFinToken;
    private static final String USER_AGENT = "[UACurrency_Bot]/1.0(http://t.me/UACurrencyBot)";


    public MinfinUpdater() {
        Properties props = new Properties();
        try {
            props.load(CurrencyBot.class.getResourceAsStream("/config.properties"));
            this.minFinToken = props.getProperty("minFinToken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Commands, Market> sendRequest(MarketType request) {
        Map<Commands, Market> result;
        if (request==MarketType.MB_MARKET) {
            result = parseMbResponse(sendGet(MarketType.MB_MARKET.toString()), MarketType.MB_MARKET);
        } else result = parseNormalResponse(sendGet(request.toString()), request);
        return result;
    }

    public Map<Commands, Market> parseNormalResponse(String response, MarketType marketType) {
        Map<Commands, Market> result = new HashMap<>();
        if (!response.equals("[]") | response.equals("")) {
            JSONObject resp = new JSONObject(response);
            result.put(Commands.USD, getAskAndBid(resp.getJSONObject("usd"), marketType));
            result.put(Commands.EURO, getAskAndBid(resp.getJSONObject("eur"), marketType));
            result.put(Commands.RUB, getAskAndBid(resp.getJSONObject("rub"), marketType));
        }
        return result;
    }

    public Map<Commands, Market> parseMbResponse(String response, MarketType marketType) {
        Map<Commands, Market> result = new HashMap<>();
        if (!response.equals("[]") | response.equals("")) {
            JSONArray array = new JSONArray(response);
            result.put(Commands.USD, getLastCurrencyMark(array, "usd", marketType));
            result.put(Commands.EURO, getLastCurrencyMark(array, "eur", marketType));
            result.put(Commands.RUB, getLastCurrencyMark(array, "rub", marketType));
        }
        return result;
    }

    @NotNull
    private String sendGet(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url + minFinToken + "/");
        request.addHeader("User-Agent", USER_AGENT);
        try {
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private Float getAsk(JSONObject object) {
        try {
            return Float.parseFloat(object.getString("ask"));
        } catch (Exception e) {
            return (float) object.getDouble("ask");
        }
    }

    private Float getBid(JSONObject object) {
        try {
            return Float.parseFloat(object.getString("bid"));
        } catch (Exception e) {
            return (float) object.getDouble("bid");
        }
    }

    private Market getAskAndBid(JSONObject object, MarketType marketType) {
        List<Float> result = Arrays.asList(getAsk(object), getBid(object));
        Collections.sort(result);
        return new Market(result.get(0), result.get(1), marketType);
    }

    /**
     * @param array
     * @param currency
     * @return
     */
    private Market getLastCurrencyMark(JSONArray array, String currency, MarketType marketType) {
        List<JSONObject> temp = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            temp.add(array.getJSONObject(i));
        }
        return getAskAndBid(temp.stream().filter(e -> e.getString("currency").equals(currency))
                .filter(e -> e.isNull("status"))
                .max(Comparator.comparingInt(o1 -> Integer.parseInt(o1.getString("id")))).get(), marketType);
    }
}
