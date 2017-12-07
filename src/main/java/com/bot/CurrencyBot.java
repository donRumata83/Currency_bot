package com.bot;

import com.bot.updaters.BitcoinUpdater;
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
import com.bot.enums.Commands;

import java.nio.charset.StandardCharsets;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static String usd;
    private static String eur;
    private static String rub;
    private static String bc;
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
     * @param dataTransformer_util
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
     * Get the update from user chat, check for not empty and send text message to user
     *
     * @param update from user chat
     */

    /*public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            sendMsg(message, messageCheck(message));
        }
    }*/

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
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /*private String messageCheck(@NotNull Message message) {
        try {
            switch (Commands.convert(message.getText().trim().toUpperCase())) {
                case START: {
                    counter++;
                    return String.format(greetingCommand, message.getFrom().getFirstName());
                }
                case HELP:
                    return helpCommand;
                case SEARCH:
                    return
                case STAT:
                    return users + counter;
                case MSTAT:
                    return requests + messageCounter;
                default:
                    return noCurrency;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noCurrency;
    }*/

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
            bc = propsMessage.getProperty("bc");
            calc = propsMessage.getProperty("calc");
            newSearch = propsMessage.getProperty("new");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
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
                        sendMsg(message, dataTransformer_util.getUSD());
                        sendMsg(message, newSearch);
                        break;
                    }
                    case "eur": {
                        sendMsg(message, dataTransformer_util.getEuro());
                        sendMsg(message, newSearch);
                        break;
                    }
                    case "rub": {
                        sendMsg(message, dataTransformer_util.getRub());
                        sendMsg(message, newSearch);
                        break;
                    }
                    case "bc": {
                        sendMsg(message, dataTransformer_util.getBTC());
                        sendMsg(message, newSearch);
                        break;
                    }
                }
            }
        }
    }

    private void getStandartKeyboard(Message message) {
        SendMessage keybordMessage = new SendMessage().setText("/new").setChatId(message.getChatId());
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstLine = new ArrayList<>();
        firstLine.add(new InlineKeyboardButton().setText(usd).setCallbackData("usd"));
        firstLine.add(new InlineKeyboardButton().setText(eur).setCallbackData("eur"));
        List<InlineKeyboardButton> secondLine = new ArrayList<>();
        secondLine.add(new InlineKeyboardButton().setText(rub).setCallbackData("rub"));
        secondLine.add(new InlineKeyboardButton().setText(bc).setCallbackData("bc"));
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
