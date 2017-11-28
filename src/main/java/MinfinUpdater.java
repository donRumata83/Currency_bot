import Enums.Market_Type;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.Properties;

public class MinfinUpdater implements Updater {
    private String minFinToken;


    MinfinUpdater() {
        Properties props = new Properties();
        try {
            props.load(Currency_Bot.class.getResourceAsStream("/config.properties"));
            this.minFinToken = props.getProperty("minFinToken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Long> sendRequest(Market_Type request) {
        ArrayList<Long> result;
        switch (request) {
            case NBU:
                result = parseNBUresponse(sendGet(Market_Type.NBU.toString()));
                break;
            case MB_MARKET:
                result = parseMBresponse(sendGet(Market_Type.MB_MARKET.toString()));
                break;
            case BANKS:
                result = parseBanksResponse(sendGet(Market_Type.BANKS.toString()));
                break;
            case AUCTION:
                result = parseAUCresponse(sendGet(Market_Type.AUCTION.toString()));
                break;
             default: result = null;
        }
        return result;
    }

    private ArrayList<Long> parseNBUresponse(String response) {
        return null;
    }

    private ArrayList<Long> parseMBresponse(String response) {
        return null;
    }

    private ArrayList<Long> parseBanksResponse(String response) {
        return null;
    }

    private ArrayList<Long> parseAUCresponse(String response) {
        return null;
    }

    private String sendGet(String url) {

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url + minFinToken + "/");

        request.addHeader("User-Agent", "[UACurrency_Bot]/1.0(http://t.me/UACurrencyBot)");
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
}
