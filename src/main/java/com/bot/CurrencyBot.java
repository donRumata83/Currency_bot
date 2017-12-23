package com.bot;

import com.bot.enums.City;
import com.bot.handlers.*;
import com.bot.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


import java.nio.charset.StandardCharsets;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main class
 */
public class CurrencyBot extends TelegramLongPollingBot {
    private String token;
    private String botName;
    private Map<Long, User> userCity;

    public long messageCounter = 0;

    private UpdateHandler standartMessageHandler;
    private UpdateHandler calcMessageHandler;
    private UpdateHandler startCitySetHandler;

    private static final int TIMEOUT_60MIN = 1000 * 60 * 60;

    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), "currency-telegram-bot");


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        CurrencyDB currency_db = new CurrencyDB();
        DataTransformerUtil dtu = new DataTransformerUtil(currency_db);
        CurrencyBot bot = new CurrencyBot();
        StandartHandler sh = new StandartHandler(bot, dtu);
        CalculatorHandler ch = new CalculatorHandler(bot, dtu);
        StartCitySetHandler sch = new StartCitySetHandler(bot);
        KeyboardSupplier ks = new KeyboardSupplier();
        CommandsSupplier cs = new CommandsSupplier();
        cs.create();
        bot.setStandartMessageHandler(sh);
        bot.setCalcMessageHandler(ch);
        bot.setStartCitySetHandler(sch);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor
     */
    private CurrencyBot() {
        loadProperties();
        this.userCity = new HashMap<>();
        loadFromFile();
        saver();
    }

    /**
     * Returns bots token
     *
     * @return String
     */
    @Override
    public String getBotToken() {
        return token;
    }

    /**
     * Returns bots name
     *
     * @return String bot name
     */
    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     * Sends message to user chat
     *
     * @param
     * @param text
     */
    public void sendMsg(@NotNull Update update, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true)
                .setChatId(update.getMessage().getChatId().toString())
                .setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the update from user chat, check for not empty and send text message to user
     *
     * @param update from user chat
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (!isCitySet(update)) startCitySetHandler.handle(update);
            else {
                if (!isCalcOn(update)) standartMessageHandler.handle(update);
                else calcMessageHandler.handle(update);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void loadProperties() {
        Properties props = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream("/config.properties");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
            props.load(reader);
            this.token = props.getProperty("token");
            this.botName = props.getProperty("botName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStandartMessageHandler(UpdateHandler standartMessageHandler) {
        this.standartMessageHandler = standartMessageHandler;
    }

    private void setCalcMessageHandler(UpdateHandler calcMessageHandler) {
        this.calcMessageHandler = calcMessageHandler;
    }

    private void setStartCitySetHandler(UpdateHandler startCitySetHandler) {
        this.startCitySetHandler = startCitySetHandler;
    }

    public City getCityForUserFromUpdate(Update update) {
        if (update.hasCallbackQuery()) return this.userCity.get(getID(update)).getCity();
        else return this.userCity.get(getID(update)).getCity();
    }

    public void putCityOfUserInMap(long id, City city) {
        userCity.put(id, new User(id, city));
    }

    public int getUserCount() {
        return userCity.size();
    }

    private boolean isCitySet(Update update) {
        return this.userCity.containsKey(getID(update));
    }

    public void removeCity(Update update) {
        this.userCity.remove(getID(update));
    }

    private boolean isCalcOn(Update update) {
        return this.userCity.get(getID(update)).isCalcOn();
    }

    public void setCalcOn(Update update) {
        this.userCity.get(getID(update)).setCalcOn(true);
    }

    public void setCalcOff(Update update) {
        this.userCity.get(getID(update)).setCalcOn(false);
    }

    private long getID(Update update) {
        long id;
        if (update.hasCallbackQuery()) id = update.getCallbackQuery().getMessage().getChatId();
        else id = update.getMessage().getChatId();
        return id;
    }

    private void saver() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(TIMEOUT_60MIN);
                    saveToFile();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(DATA_STORE_DIR + "/users.json"), "UTF8"))) {
            JSONObject tmp;
            JSONArray array = new JSONArray();
            for (long id : userCity.keySet()) {
                tmp = new JSONObject();
                tmp.put("id", id);
                tmp.put("city", userCity.get(id).getCity().getName());
                tmp.put("isCalcOn", userCity.get(id).isCalcOn());
                array.put(tmp);
                System.out.println(tmp);
            }
            writer.write(array.toString());
            writer.flush();
            System.out.println("File are saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try {
            String text = new String(Files.readAllBytes(Paths.get(DATA_STORE_DIR + "/users.json")), "UTF-8");
            JSONArray array = new JSONArray(text);
            array.forEach(user -> parseAndAddUser((JSONObject) user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseAndAddUser(JSONObject jUser) {
        User user = new User();
        user.setId(jUser.getLong("id"));
        user.setCity(City.getCity(jUser.getString("city")));
        user.setCalcOn(jUser.getBoolean("isCalcOn"));
        userCity.put(user.getId(), user);
    }
}
