package com.bot;

import com.bot.updaters.BitcoinUpdater;
import com.bot.updaters.MinfinUpdater;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.bot.enums.Commands;


import javax.validation.constraints.NotNull;
import java.io.*;
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

    private String helpCommand;
    private String greetingCommand;

    private static final File DATA_STORE_DIR = new File(
            System.getProperty("user.home"), "currency-telegram-bot");

    private static final String CONFIG_PROPERTIES = File.pathSeparatorChar + "config.properties";
    private static final String MESSAGE_POPERTIES = File.pathSeparatorChar + "message.properties";

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
        Properties props = new Properties();
        Properties propsMessage = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream(MESSAGE_POPERTIES);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            props.load(reader);
            this.token = props.getProperty("token");
            this.botName = props.getProperty("botName");
            in = getClass().getResourceAsStream(MESSAGE_POPERTIES);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            propsMessage.load(reader);
            this.helpCommand = propsMessage.getProperty("help");
            this.greetingCommand = propsMessage.getProperty("greeting");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            sendMsg(message, messageCheck(message));
        }

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
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert user request and returns text for sending to user
     *
     * @param message checked not null message
     * @return String text for sending
     */
    private String messageCheck(@NotNull Message message) {
        switch (Commands.convert(message.getText().trim().toUpperCase())) {
            case START: {
                counter++;
                return String.format(greetingCommand, message.getFrom().getFirstName());
            }
            case HELP:
                return helpCommand;
            case USD: {
                messageCounter++;
                return dataTransformer_util.getUSD();
            }
            case EURO: {
                messageCounter++;
                return dataTransformer_util.getEuro();
            }
            case RUB: {
                messageCounter++;
                return dataTransformer_util.getRub();
            }
            case BTC: {
                messageCounter++;
                return dataTransformer_util.getBTC();
            }
            case STAT:
                return "Юзеров: " + counter;
            case MSTAT:
                return "Запросов: " + messageCounter;
            default:
                return "Извините, не могу ответить на это сообщение. Попробуйте еще раз.";
        }
    }
}
