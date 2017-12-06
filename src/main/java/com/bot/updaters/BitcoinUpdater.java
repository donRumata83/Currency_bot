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
import java.util.List;

public class BitcoinUpdater implements Updater{
    @Override
    public List<Float> sendRequest(MarketType request) {
        List<Float> result;
        result = parse(sendGet(MarketType.BTC.toString()));
        return result;
    }

    @NotNull
    private String sendGet(String url) {
        URL httpsUrl;
        try {

            httpsUrl = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection)httpsUrl.openConnection();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<Float> parse(String response) {
        List<Float> result = new ArrayList<>();
        if (!response.equals("{}") | response.equals("")) {
            JSONObject resp = new JSONObject(response);
            result.add(Float.parseFloat(resp.getJSONObject("ticker").getString("price")));
        }
        return result;
    }
}
