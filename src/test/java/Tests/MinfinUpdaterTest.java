package Tests;


import Bot.Enums.MarketType;
import Bot.MinfinUpdater;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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

    @Test
    public void checkMBresponseParsing() {
        //given
        MinfinUpdater updater = new MinfinUpdater();

        ArrayList<Float> result = updater.parseMbResponse(MB_response);

        System.out.println(result);

    }


    @Test
    public void checkAUC_responseParsing()
    {
        MinfinUpdater updater = new MinfinUpdater();

        ArrayList<Float> result = updater.parseNormalResponse(AUC_response);

        System.out.println(result);
    }


    @Test
    public void checkNBU_ResponseParsing()
    {
        MinfinUpdater updater = new MinfinUpdater();

        ArrayList<Float> result = updater.parseNormalResponse(NBU_response);

        System.out.println(result);
    }

    @Test
    public void checkServerResponse()
    {
        MinfinUpdater updater = new MinfinUpdater();

        ArrayList<Float> result = updater.sendRequest(MarketType.MB_MARKET);

        System.out.println(result);

    }
}
