package Tests;

import com.bot.updaters.AuctionUpdater;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

public class AuctionParseTest {
    @Test
    public void textGettingTest() {
        AuctionUpdater au = new AuctionUpdater();

        try {
            System.out.println(au.getMarket("https://minfin.com.ua/currency/auction/usd/buy/kiev/"));

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
