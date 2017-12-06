package bot;

import bot.currencies.Currency;
import bot.enums.Commands;
import bot.enums.MarketType;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.validation.constraints.NotNull;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencyDB {
    private HashMap<Commands, Currency> actualCurrencyStorage;
    private Updater updater;

    private static final int TIMEOUT_5MIN = 1000 * 60 * 5;
    private static final int TIMEOUT_1HOUR = (1000 * 60 * 60) + 100;


    CurrencyDB(Updater updater) {
        try (InputStream reader = CurrencyBot.class.getResourceAsStream("/currency.properties")) {
            ObjectMapper maper = new ObjectMapper();
            Properties props = new Properties();
            props.load(reader);
            this.actualCurrencyStorage = new HashMap<>();
            this.actualCurrencyStorage.put(Commands.USD, maper.readValue(props.getProperty("usd"), Currency.class));
            this.actualCurrencyStorage.put(Commands.EURO, maper.readValue(props.getProperty("eur"), Currency.class));
            this.actualCurrencyStorage.put(Commands.RUB, maper.readValue(props.getProperty("rub"), Currency.class));
            ;
        } catch (Exception e) {
            e.printStackTrace();
            this.actualCurrencyStorage = new HashMap<Commands, Currency>() {{
                put(Commands.USD, new Currency("Доллар США"));
                put(Commands.EURO, new Currency("Евро"));
                put(Commands.RUB, new Currency("Десять Российских рублей"));
            }};
        }
        this.updater = updater;
        update();
        saveTimer();
    }

    public HashMap<Commands, Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
    }

    private void update() {
        Thread run = new Thread(() -> {
            Deque<MarketType> request_queue = new ArrayDeque<>
                    (Arrays.asList(MarketType.MB_MARKET, MarketType.NBU, MarketType.BANKS, MarketType.AUCTION));
            while (true) {
                try {
                    MarketType request = request_queue.pollFirst();
                    List<Float> response = updater.sendRequest(request);
                    System.out.println(response);
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                    switch (request) {
                        case NBU:
                            updateNBU(response);
                            request_queue.addLast(request);
                            break;
                        case AUCTION:
                            updateAUC(response);
                            request_queue.addLast(request);
                            break;
                        case BANKS:
                            updateBank(response);
                            request_queue.addLast(request);
                            break;
                        case MB_MARKET:
                            updateMB(response);
                            request_queue.addLast(request);
                            break;
                    }
                    Thread.sleep(TIMEOUT_5MIN);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        run.start();
    }

    private void updateMB(@NotNull List<Float> response) {
        if (response.size() != 0) {
            Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setMb_ask(response.get(0));
            usd.setMb_bid(response.get(1));
            actualCurrencyStorage.put(Commands.USD, usd);

            Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setMb_ask(response.get(2));
            euro.setMb_bid(response.get(3));
            actualCurrencyStorage.put(Commands.EURO, euro);

            Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setMb_ask(response.get(4) * 10);
            rub.setMb_bid(response.get(5) * 10);
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateBank(@NotNull List<Float> response) {
        if (response.size() != 0) {
            Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setBank_ask(response.get(0));
            usd.setBank_bid(response.get(1));
            actualCurrencyStorage.put(Commands.USD, usd);

            Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setBank_ask(response.get(2));
            euro.setBank_bid(response.get(3));
            actualCurrencyStorage.put(Commands.EURO, euro);

            Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setBank_ask(response.get(4) * 10);
            rub.setBank_bid(response.get(5) * 10);
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateAUC(@NotNull List<Float> response) {
        if (response.size() != 0) {
            Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setAuc_ask(response.get(0));
            usd.setAuc_bid(response.get(1));
            actualCurrencyStorage.put(Commands.USD, usd);

            Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setAuc_ask(response.get(2));
            euro.setAuc_bid(response.get(3));
            actualCurrencyStorage.put(Commands.EURO, euro);

            Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setAuc_ask(response.get(4) * 10);
            rub.setAuc_bid(response.get(5) * 10);
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateNBU(@NotNull List<Float> response) {
        if (response.size() != 0) {
            Currency usd = actualCurrencyStorage.get(Commands.USD);
            usd.setNbu_ask(response.get(0));
            usd.setNbu_bid(response.get(1));
            actualCurrencyStorage.put(Commands.USD, usd);

            Currency euro = actualCurrencyStorage.get(Commands.EURO);
            euro.setNbu_ask(response.get(2));
            euro.setNbu_bid(response.get(3));
            actualCurrencyStorage.put(Commands.EURO, euro);

            Currency rub = actualCurrencyStorage.get(Commands.RUB);
            rub.setNbu_ask(response.get(4) * 10);
            rub.setNbu_bid(response.get(5) * 10);
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void saveTimer() {
        new Thread(() -> {

            try {
                while (true) {
                    Thread.sleep(TIMEOUT_1HOUR);
                    saveInFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void saveInFile() {
        try (FileOutputStream fis = new FileOutputStream(CurrencyBot.class.getResource("/currency.properties").getFile(), false);) {
            ObjectMapper mapper = new ObjectMapper();
            Properties props = new Properties();
            StringWriter stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, actualCurrencyStorage.get(Commands.USD));
            props.setProperty("usd", stringWriter.toString());
            stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, actualCurrencyStorage.get(Commands.EURO));
            props.setProperty("eur", stringWriter.toString());
            stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, actualCurrencyStorage.get(Commands.RUB));
            props.setProperty("rub", stringWriter.toString());
            props.store(fis, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}