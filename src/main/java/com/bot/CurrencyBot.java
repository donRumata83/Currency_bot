package com.bot;

import com.bot.handlers.BotHandler;
import com.bot.handlers.CalculatorHandler;
import com.bot.handlers.KeyboardSupplier;
import com.bot.handlers.StandartHandler;
import com.bot.updaters.BitcoinUpdater;
import com.bot.updaters.MinfinUpdater;
import com.bot.updaters.NBUUpdater;
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

    public long counter = 0;
    public long messageCounter = 0;


    private BotHandler standartMessageHandler;
    private BotHandler calcMessageHandler;
    public boolean isCalcOn = false;


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        CurrencyDB currency_db = new CurrencyDB(new MinfinUpdater(), new BitcoinUpdater(), new NBUUpdater());
        DataTransformerUtil dtu = new DataTransformerUtil(currency_db);
        CurrencyBot bot = new CurrencyBot();
        StandartHandler sh = new StandartHandler(bot, dtu);
        CalculatorHandler ch = new CalculatorHandler(bot, dtu);
        KeyboardSupplier ks = new KeyboardSupplier();
        bot.setStandartMessageHandler(sh);
        bot.setCalcMessageHandler(ch);
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
            if (!isCalcOn) standartMessageHandler.handle(update);
            else calcMessageHandler.handle(update);
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
}
