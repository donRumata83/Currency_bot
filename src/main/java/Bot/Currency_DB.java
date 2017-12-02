package Bot;

import Bot.Currencies.Currency;
import Bot.Enums.Commands;
import Bot.Enums.Mark;
import Bot.Enums.Market_Type;

import java.text.SimpleDateFormat;
import java.util.*;

public class Currency_DB {
    private HashMap<Commands, Currency> actualCurrencyStorage;
    private Updater updater;

    private static final int TIMEOUT_5MIN = 1000 * 60 * 5;


    Currency_DB(Updater updater) {
        this.actualCurrencyStorage = new HashMap<>();
        actualCurrencyStorage.put(Commands.USD, new Currency("Доллар США", Mark.USD));
        actualCurrencyStorage.put(Commands.EURO, new Currency("Евро", Mark.EUR));
        actualCurrencyStorage.put(Commands.RUB, new Currency("Российский рубль", Mark.RUB));
        this.updater = updater;
        update();
    }

    public HashMap<Commands, Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
    }

    private void update() {
        Thread run = new Thread(() -> {
            Deque<Market_Type> request_queue = new ArrayDeque<>();
            request_queue.add(Market_Type.MB_MARKET);
            request_queue.add(Market_Type.NBU);
            request_queue.add(Market_Type.BANKS);
            request_queue.add(Market_Type.AUCTION);
            while (true) {
                try {
                    Market_Type request = request_queue.pollFirst();
                    System.out.println(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
                    ArrayList<Float> response = updater.sendRequest(request);
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

    private void updateMB(ArrayList<Float> response) {
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
            rub.setMb_ask(response.get(4));
            rub.setMb_bid(response.get(5));
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateBank(ArrayList<Float> response) {
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
            rub.setBank_ask(response.get(4));
            rub.setBank_bid(response.get(5));
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateAUC(ArrayList<Float> response) {
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
            rub.setAuc_ask(response.get(4));
            rub.setAuc_bid(response.get(5));
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }

    private void updateNBU(ArrayList<Float> response) {
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
            rub.setNbu_ask(response.get(4));
            rub.setNbu_bid(response.get(5));
            actualCurrencyStorage.put(Commands.RUB, rub);
        }
    }


}
