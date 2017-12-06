package Bot;

import Bot.Enums.MarketType;

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
    public List<Float> sendRequest(MarketType request) {
        List<Float> result;
        switch (request) {
            case NBU:
                result = parseNormalResponse(sendGet(MarketType.NBU.toString()));
                break;
            case MB_MARKET:
                result = parseMbResponse(sendGet(MarketType.MB_MARKET.toString()));
                break;
            case BANKS:
                result = parseNormalResponse(sendGet(MarketType.BANKS.toString()));
                break;
            case AUCTION:
                result = parseNormalResponse(sendGet(MarketType.AUCTION.toString()));
                break;
            default:
                result = null;
        }
        return result;
    }

    public List<Float> parseNormalResponse(String response) {
        List<Float> result = new ArrayList<>();
        if (!response.equals("[]") | response.equals("")) {
            JSONObject resp = new JSONObject(response);
            result.addAll(getAskAndBid(resp.getJSONObject("usd")));
            result.addAll(getAskAndBid(resp.getJSONObject("eur")));
            result.addAll(getAskAndBid(resp.getJSONObject("rub")));
        }
        return result;
    }

    public List<Float> parseMbResponse(String response) {
        List<Float> result = new ArrayList<>();
        if (!response.equals("[]") | response.equals("")) {
            JSONArray array = new JSONArray(response);
            result.addAll(getLastCurrencyMark(array, "usd"));
            result.addAll(getLastCurrencyMark(array, "eur"));
            result.addAll(getLastCurrencyMark(array, "rub"));
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

    private List<Float> getAskAndBid(JSONObject object) {
        List<Float> result = Arrays.asList(getAsk(object), getBid(object));
        Collections.sort(result);
        return result;
    }

    /**
     * @param array
     * @param currency
     * @return
     */
    private List<Float> getLastCurrencyMark(JSONArray array, String currency) {
        List<JSONObject> temp = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            temp.add(array.getJSONObject(i));
        }
        return getAskAndBid(temp.stream().filter(e -> e.getString("currency").equals(currency))
                .filter(e -> e.isNull("status"))
                .max(Comparator.comparingInt(o1 -> Integer.parseInt(o1.getString("id")))).get());
    }
}
