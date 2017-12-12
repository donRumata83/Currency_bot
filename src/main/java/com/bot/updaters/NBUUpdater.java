package com.bot.updaters;


import com.bot.currencies.SimpleCurrency;
import com.bot.enums.MarketType;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NBUUpdater {

    public List<SimpleCurrency> sendRequest(MarketType request) {

        return parse(sendGet(request.toString()));
    }

    private List<SimpleCurrency> parse(String response) {
        List<SimpleCurrency> result = new ArrayList<>();
        JSONObject temp;
        if (!response.equals("[]")) {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                temp = array.getJSONObject(i);
                result.add(new SimpleCurrency(temp.getString("txt"), (float) temp.getDouble("rate"), temp.getString("cc")));
            }
        }
        return result;
    }

    private String sendGet(String url) {
        URL httpsUrl;
        try {
            httpsUrl = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) httpsUrl.openConnection();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[]";
    }


}
