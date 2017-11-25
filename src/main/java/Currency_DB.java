import Currencies.*;
import Enums.Commands;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class Currency_DB {
    private HashMap<Commands, Currency> actualCurrencyStorage;
    private Updater updater;
    private Queue<String> request_queue;

    private final String mb_request = "http://api.minfin.com.ua/mb//";
    private final String nbu_request = "http://api.minfin.com.ua/nbu//";
    private final String banks_request = "http://api.minfin.com.ua/summary//";
    private final String auc_request = "http://api.minfin.com.ua/auction/info//";


    public Currency_DB() {
        this.actualCurrencyStorage = new HashMap<>();
        actualCurrencyStorage.put(Commands.USD, new Currency("Доллар США"));
        actualCurrencyStorage.put(Commands.EURO, new Currency("Евро"));
        actualCurrencyStorage.put(Commands.RUB, new Currency("Российский рубль"));
        actualCurrencyStorage.put(Commands.GBP, new Currency("Британский фунт стерлингов"));
        this.request_queue = new PriorityQueue<>();
        request_queue.add(mb_request);
        request_queue.add(nbu_request);
        request_queue.add(banks_request);
        request_queue.add(auc_request);

        update();
    }

    public HashMap<Commands, Currency> getActualCurrencyStorage() {
        return actualCurrencyStorage;
    }

    private void update () {
        while (true) {
            int delay = 300000;
            ActionListener taskPerformer = evt -> {
                String request = request_queue.poll();
                String answer = updater.sendRequest(request);
                decompileJSON(answer);
                request_queue.add(request);
            };
            new Timer(delay, taskPerformer).start();
        }
    }

    private void decompileJSON(String text) {

    }


}
