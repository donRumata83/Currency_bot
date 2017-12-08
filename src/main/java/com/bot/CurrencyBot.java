package com.bot;

import com.bot.updaters.BitcoinUpdater;
import com.bot.updaters.FakeUpdater;
import com.bot.updaters.MinfinUpdater;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


import java.nio.charset.StandardCharsets;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Main class
 */
public class CurrencyBot extends TelegramLongPollingBot {
    private String token;
    private String botName;
    private DataTransformerUtil dataTransformer_util;
    private static long counter = 0;
    private static long messageCounter = 0;

    private static String helpCommand;
    private static String greetingCommand;
    private static String newSearch;
    private static String users;
    private static String requests;
    private static String noCurrency;
    private static String newCurrencyRequestMessage;

    private static String usd;
    private static String eur;
    private static String rub;
    private static String btc;
    private static String calc;


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        CurrencyDB currency_db = new CurrencyDB(new MinfinUpdater(), new BitcoinUpdater());
        try {
            botsApi.registerBot(new CurrencyBot(new DataTransformerUtil(currency_db)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor
     *
     * @param dataTransformer_util gets
     */
    private CurrencyBot(DataTransformerUtil dataTransformer_util) {
        this.dataTransformer_util = dataTransformer_util;
        StandardCharsets.UTF_8.name();
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
    private void sendMsg(@NotNull Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true)
                .setChatId(message.getChatId().toString())
                .setReplyToMessageId(message.getMessageId())
                .setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageWithQuery(Update update, String text) {
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

    private void loadProperties() {
        Properties props = new Properties();
        Properties propsMessage = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream("/config.properties");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
            props.load(reader);
            this.token = props.getProperty("token");
            this.botName = props.getProperty("botName");
            in = getClass().getResourceAsStream("/message.properties");
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
            propsMessage.load(reader);
            helpCommand = propsMessage.getProperty("help");
            greetingCommand = propsMessage.getProperty("greeting");
            users = propsMessage.getProperty("users");
            requests = propsMessage.getProperty("requests");
            noCurrency = propsMessage.getProperty("nocurrency");
            usd = propsMessage.getProperty("usd");
            eur = propsMessage.getProperty("eur");
            rub = propsMessage.getProperty("rub");
            btc = propsMessage.getProperty("btc");
            calc = propsMessage.getProperty("calc");
            newSearch = propsMessage.getProperty("new");
            newCurrencyRequestMessage = propsMessage.getProperty("newreq");
        } catch (Exception e) {
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
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            String message_text = update.getMessage().getText();
            switch (message_text) {
                case "/start": {
                    counter++;
                    sendMsg(message, String.format(greetingCommand, message.getFrom().getFirstName()));
                    sendMsg(message, newSearch);
                    break;
                }
                case "/help": {
                    sendMsg(message, helpCommand);
                    sendMsg(message, newSearch);
                    break;
                }
                case "users": {
                    sendMsg(message, users + counter);
                    sendMsg(message, newSearch);
                    break;
                }
                case "/new": {
                    messageCounter++;
                    getStandartKeyboard(message);
                    break;
                }
                case "/mstat": {
                    sendMsg(message, requests + messageCounter);
                    sendMsg(message, newSearch);
                    break;
                }
                default: {
                    sendMsg(message, noCurrency);
                    sendMsg(message, newSearch);
                    break;
                }

            }
        } else {
            if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                switch (data) {
                    case "usd": {
                        sendMessageWithQuery(update, dataTransformer_util.getUSD());
                        sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "eur": {
                        sendMessageWithQuery(update, dataTransformer_util.getEuro());
                        sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "rub": {
                        sendMessageWithQuery(update, dataTransformer_util.getRub());
                        sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "btc": {
                        sendMessageWithQuery(update, dataTransformer_util.getBTC());
                        sendMessageWithQuery(update, newSearch);
                        break;
                    }
                }
            }
        }
    }

    private void getStandartKeyboard(Message message) {
        SendMessage keybordMessage = new SendMessage()
                .setChatId(message.getChatId())
                .setText(newCurrencyRequestMessage);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstLine = new ArrayList<>();
        firstLine.add(new InlineKeyboardButton().setText(usd).setCallbackData("usd"));
        firstLine.add(new InlineKeyboardButton().setText(eur).setCallbackData("eur"));
        List<InlineKeyboardButton> secondLine = new ArrayList<>();
        secondLine.add(new InlineKeyboardButton().setText(rub).setCallbackData("rub"));
        secondLine.add(new InlineKeyboardButton().setText(btc).setCallbackData("btc"));
        rows.add(firstLine);
        rows.add(secondLine);
        markupInline.setKeyboard(rows);
        keybordMessage.setReplyMarkup(markupInline);
        try {
            execute(keybordMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
