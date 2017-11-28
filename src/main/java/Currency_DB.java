import Currencies.*;
import Enums.Commands;
import Enums.Market_Type;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class Currency_DB {
    private HashMap<Commands, Currency> actualCurrencyStorage;
    private Updater updater;
    private Queue<Market_Type> request_queue;

    private final String mb_request = "http://api.minfin.com.ua/mb//";
    private final String nbu_request = "http://api.minfin.com.ua/nbu//";
    private final String banks_request = "http://api.minfin.com.ua/summary//";
    private final String auc_request = "http://api.minfin.com.ua/auction/info//";

    private static final int TIMEOUT_5MIN = 1000*60*5;


    public Currency_DB(MinfinUpdater updater) {
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
                updater.sendRequest(request);
                request_queue.add(request);
            };
            new Timer(TIMEOUT_5MIN, taskPerformer).start();
        }
    }




}
