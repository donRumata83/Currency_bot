package com.bot.handlers;

import com.bot.CurrencyBot;
import com.bot.DataTransformerUtil;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class StandartHandler implements BotHandler {
    private CurrencyBot bot;
    private DataTransformerUtil dtu;

    private static String helpCommand;
    private static String greetingCommand;
    private static String newSearch;
    private static String users;
    private static String requests;
    private static String noCurrency;


    public StandartHandler(CurrencyBot bot, DataTransformerUtil util) {
        this.bot = bot;
        this.dtu = util;
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handle(Update update) throws TelegramApiException {
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            String message_text = update.getMessage().getText();
            switch (message_text) {
                case "/start": {
                    bot.counter++;
                    bot.sendMsg(message, String.format(greetingCommand, message.getFrom().getFirstName()));
                    bot.execute(KeyboardSupplier.getStandartKeyboard(message));
                    break;
                }
                case "/help": {
                    bot.sendMsg(message, helpCommand);
                    bot.sendMsg(message, newSearch);
                    break;
                }
                case "users": {
                    bot.sendMsg(message, users + bot.counter);
                    bot.sendMsg(message, newSearch);
                    break;
                }
                case "/new": {
                    bot.messageCounter++;
                    bot.isCalcOn = false;
                    bot.execute(KeyboardSupplier.getStandartKeyboard(message));
                    break;
                }
                case "/mstat": {
                    bot.sendMsg(message, requests + bot.messageCounter);
                    bot.sendMsg(message, newSearch);
                    break;
                }
                default: {
                    bot.sendMsg(message, noCurrency);
                    bot.sendMsg(message, newSearch);
                    break;
                }
            }
        } else {
            if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                switch (data) {
                    case "usd": {
                        bot.sendMessageWithQuery(update, dtu.getUSD());
                        bot.sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "eur": {
                        bot.sendMessageWithQuery(update, dtu.getEuro());
                        bot.sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "rub": {
                        bot.sendMessageWithQuery(update, dtu.getRub());
                        bot.sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "btc": {
                        bot.sendMessageWithQuery(update, dtu.getBTC());
                        bot.sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "other": {
                        bot.sendMessageWithQuery(update, dtu.getOtherCurrencyFirstHalf());
                        bot.sendMessageWithQuery(update, dtu.getOtherCurrencySecondHalf());
                        bot.sendMessageWithQuery(update, newSearch);
                        break;
                    }
                    case "calc": {
                        bot.isCalcOn = true;
                        bot.execute(KeyboardSupplier.getCalcKeybord(update));
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void loadProperties() throws IOException {
        Properties propsMessage = new Properties();
        InputStream in = getClass().getResourceAsStream("/message.properties");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
        propsMessage.load(reader);
        helpCommand = propsMessage.getProperty("help");
        greetingCommand = propsMessage.getProperty("greeting");
        users = propsMessage.getProperty("users");
        requests = propsMessage.getProperty("requests");
        noCurrency = propsMessage.getProperty("nocurrency");
        newSearch = propsMessage.getProperty("new");
    }
}
