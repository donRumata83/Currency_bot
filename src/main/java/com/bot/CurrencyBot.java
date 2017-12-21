package com.bot;

import com.bot.enums.City;
import com.bot.handlers.*;
import com.bot.updaters.BitcoinUpdater;
import com.bot.updaters.MinfinUpdater;
import com.bot.updaters.NBUUpdater;
import com.bot.users.User;
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
import java.util.*;

/**
 * Main class
 */
public class CurrencyBot extends TelegramLongPollingBot {
    private String token;
    private String botName;
    private Map<Long, User> userCity;

    public long messageCounter = 0;

    private BotHandler standartMessageHandler;
    private BotHandler calcMessageHandler;
    private BotHandler startCitySetHandler;



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
     * @param message from user
     * @param text
     */
    public void sendMsg(@NotNull Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true)
                .setChatId(message.getChatId().toString())
                .setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageWithQuery(Update update, String text) {
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        SendMessage new_message = new SendMessage()
                .setChatId(chat_id)
                .setText(text)
                .enableMarkdown(true);
        try {
            execute(new_message);
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

    private void setStandartMessageHandler(BotHandler standartMessageHandler) {
        this.standartMessageHandler = standartMessageHandler;
    }

    private void setCalcMessageHandler(BotHandler calcMessageHandler) {
        this.calcMessageHandler = calcMessageHandler;
    }

    private void setStartCitySetHandler(BotHandler startCitySetHandler) {
        this.startCitySetHandler = startCitySetHandler;
    }

    public City getCityForUserFromUpdate(Update update) {
        if (update.hasCallbackQuery()) return this.userCity.get(getID(update)).getCity();
        else return this.userCity.get(getID(update)).getCity();
    }

    public void putCityOfUserInMap(long id, City city) {
        userCity.put(id, new User(city));
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

    public void setCalcOff(Update update) {this.userCity.get(getID(update)).setCalcOn(false);}

    private long getID(Update update) {
        long id;
        if (update.hasCallbackQuery()) id = update.getCallbackQuery().getMessage().getChatId();
        else id = update.getMessage().getChatId();
        return id;
    }
}
