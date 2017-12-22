package com.bot.handlers;

import com.bot.CurrencyBot;
import com.bot.enums.City;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class StartCitySetHandler implements UpdateHandler {
    private CurrencyBot bot;

    private static String greetingCommand;
    private static String newSearch;
    private static String citySet;

    public StartCitySetHandler(CurrencyBot bot) {
        this.bot = bot;
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
                    bot.sendMsg(message, String.format(greetingCommand, message.getFrom().getFirstName()));
                    bot.execute(KeyboardSupplier.getCityKeyboard(update));
                    break;
                }
                default: {
                    bot.putCityOfUserInMap(update.getMessage().getChatId(), City.getCity(message_text));
                    bot.sendMsg(update.getMessage(), String.format(citySet, message_text));
                    bot.sendMsg(update.getMessage(), newSearch);
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
        greetingCommand = propsMessage.getProperty("greeting");
        newSearch = propsMessage.getProperty("new");
        citySet = propsMessage.getProperty("citySet");
    }
}
