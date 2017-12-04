package Bot;

import Bot.Enums.Market_Type;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

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
            props.load(Currency_Bot.class.getResourceAsStream("/config.properties"));
            this.minFinToken = props.getProperty("minFinToken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Float> sendRequest(Market_Type request) {
        ArrayList<Float> result;
        switch (request) {
            case NBU:
                result = parseNormalResponse(sendGet(Market_Type.NBU.toString()));
                break;
            case MB_MARKET:
                result = parse_MB_Response(sendGet(Market_Type.MB_MARKET.toString()));
                break;
            case BANKS:
                result = parseNormalResponse(sendGet(Market_Type.BANKS.toString()));
                break;
            case AUCTION:
                result = parseNormalResponse(sendGet(Market_Type.AUCTION.toString()));
                break;
            default:
                result = null;
        }
        return result;
    }

    public ArrayList<Float> parseNormalResponse(String response) {
        ArrayList<Float> result = new ArrayList<>();
        if (!response.equals("[]") | response.equals("")) {
            JSONObject resp = new JSONObject(response);
            result.addAll(orderCheck(getAsk(resp.getJSONObject("usd")), getBid(resp.getJSONObject("usd"))));

            result.addAll(orderCheck(getAsk(resp.getJSONObject("eur")), getBid(resp.getJSONObject("eur"))));

            result.addAll(orderCheck(getAsk(resp.getJSONObject("rub")), getBid(resp.getJSONObject("rub"))));

        }
        return result;
    }

    public ArrayList<Float> parse_MB_Response(String response) {
        ArrayList<Float> result = new ArrayList<>();
        if (!response.equals("[]") | response.equals("")) {
            JSONArray array = new JSONArray(response);
            result.addAll(getLastCurrencyMark(array, "usd"));

            result.addAll(getLastCurrencyMark(array, "eur"));

            result.addAll(getLastCurrencyMark(array, "rub"));

        }
        return result;
    }


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

    private ArrayList<Float> orderCheck(float first, float second) {
        Float[] arr = {first, second};
        ArrayList<Float> result = new ArrayList<>(Arrays.asList(arr));
        Collections.sort(result);
        return result;
    }

    private ArrayList<Float> getLastCurrencyMark(JSONArray array, String currency) {
        ArrayList<JSONObject> temp = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            if (array.getJSONObject(i).get("currency").equals(currency) && array.getJSONObject(i).isNull("status")) {
                temp.add(array.getJSONObject(i));
            }
        }
        Collections.sort(temp, (Comparator) (o1, o2) -> Integer.parseInt(((JSONObject) o1).getString("id")) -
                Integer.parseInt(((JSONObject) o2).getString("id")));
        JSONObject goal = temp.get(temp.size() - 1);

        return orderCheck(getAsk(goal),getBid(goal));
    }
}
