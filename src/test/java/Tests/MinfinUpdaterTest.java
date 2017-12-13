package Tests;


import com.bot.currencies.Market;
import com.bot.currencies.SimpleCurrency;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;
import com.bot.updaters.MinfinUpdater;
import com.bot.updaters.NBUUpdater;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class MinfinUpdaterTest {
    private String MB_response;
    private String NBU_response;
    private String AUC_response;
    private String Banks_response;


    @Before
    public void before() {
        Properties props = new Properties();
        try {
            props.load(MinfinUpdaterTest.class.getResourceAsStream("/mb_response.txt"));
            this.MB_response = props.getProperty("mb");
            this.NBU_response = props.getProperty("nbu");
            this.AUC_response = props.getProperty("auc");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
