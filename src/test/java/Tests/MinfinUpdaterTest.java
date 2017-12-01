package Tests;


import Bot.MinfinUpdater;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void checkMBresponseParsing() {
        //given
        MinfinUpdater updater = new MinfinUpdater();

        ArrayList<Float> result = updater.parseMBresponse(MB_response);



    }

    @Test
    public void checkNBU_responseParse()
    {
        MinfinUpdater updater = new MinfinUpdater();

        ArrayList<Float> result = updater.parseNBUresponse(NBU_response);

        System.out.println(result);
    }

}
