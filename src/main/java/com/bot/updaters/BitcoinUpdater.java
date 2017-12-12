package com.bot.updaters;

import com.bot.enums.MarketType;
import org.json.JSONObject;


import javax.net.ssl.HttpsURLConnection;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BitcoinUpdater implements Updater {

    @Override
    public List<Float> sendRequest(MarketType request) {
        return parse(sendGet(MarketType.BTC.toString()));
    }

    @NotNull
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<Float> parse(String response) {
        try {
            List<Float> result = new ArrayList<>();
            if (!response.equals("{}") | response.equals("")) {
                JSONObject resp = new JSONObject(response);
                result.add(Float.parseFloat(resp.getJSONObject("ticker").getString("price")));
            }
            return result;
        } catch (Exception e) {e.printStackTrace();}
        return Collections.singletonList(0.0f);

    }
}
