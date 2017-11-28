import Currencies.Currency;
import Enums.Commands;
import Enums.Market_Type;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class MinfinUpdater implements Updater{
    private String minFinToken;

    @Override
    public HashMap<Commands, Currency> sendRequest(Market_Type request) {
        return null;
    }

    public MinfinUpdater() {
        Properties props = new Properties();
        try {
            props.load(Currency_Bot.class.getResourceAsStream("/config.properties"));
            this.minFinToken = props.getProperty("minFinToken");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
