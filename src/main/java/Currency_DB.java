import Currencies.*;
import Enums.Commands;
import Enums.Market_Type;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class Currency_DB {
    private HashMap<Commands, Currency> actualCurrencyStorage;
    private Updater updater;
    private Queue<Market_Type> request_queue;

        private static final int TIMEOUT_5MIN = 1000*60*5;


    public Currency_DB(Updater updater) {
        this.actualCurrencyStorage = new HashMap<>();
        actualCurrencyStorage.put(Commands.USD, new Currency("Доллар США"));
        actualCurrencyStorage.put(Commands.EURO, new Currency("Евро"));
        actualCurrencyStorage.put(Commands.RUB, new Currency("Российский рубль"));
        this.request_queue = new PriorityQueue<>();
        request_queue.add(Market_Type.MB_MARKET);
        request_queue.add(Market_Type.NBU);
        request_queue.add(Market_Type.BANKS);
        request_queue.add(Market_Type.AUCTION);
        this.updater = updater;
        update();
    }

    public HashMap<Commands, Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
    }

    private void update () {
        while (true) {

            ActionListener taskPerformer = evt -> {
                Market_Type request = request_queue.poll();
                ArrayList<Float> response = updater.sendRequest(request);
                request_queue.add(request);
                switch (request) {
                    case NBU: updateNBU(response);
                    break;
                    case AUCTION: updateAUC(response);
                    break;
                    case BANKS: updateBank(response);
                    break;
                    case MB_MARKET: updateMB(response);
                }
            };
            new Timer(TIMEOUT_5MIN, taskPerformer).start();

        }
    }

    private void updateMB(ArrayList<Float> response) {
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

    private void updateBank(ArrayList<Float> response) {
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

    private void updateAUC(ArrayList<Float> response) {
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

    private void updateNBU(ArrayList<Float> response) {
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
