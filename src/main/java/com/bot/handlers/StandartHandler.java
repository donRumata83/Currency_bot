package com.bot.handlers;

import com.bot.CurrencyBot;
import com.bot.DataTransformerUtil;

import com.bot.enums.Commands;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class StandartHandler implements UpdateHandler {
    private CurrencyBot bot;
    private DataTransformerUtil dtu;

    private static String helpCommand;
    private static String users;
    private static String requests;

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
            Commands command = CommandsSupplier.getComand(message_text);
            switch (command) {
                case HELP: {
                    bot.sendMsg(update, helpCommand);
                    break;
                }
                case STAT: {
                    bot.sendMsg(update, users + bot.getUserCount());
                    break;
                }
                case NEW: {
                    bot.messageCounter++;
                    bot.setCalcOff(update);
                    bot.execute(KeyboardSupplier.getStandartKeyboard(update));
                    break;
                }
                case MSTAT: {
                    bot.sendMsg(update, requests + bot.messageCounter);
                    break;
                }
                case START:
                    bot.execute(KeyboardSupplier.getCityKeyboard(update));
                    bot.removeCity(update);
                    break;
                case USD: {
                    bot.sendMsg(update, dtu.getUSD(bot.getCityForUserFromUpdate(update)));
                    break;
                }
                case EURO: {
                    bot.sendMsg(update, dtu.getEuro(bot.getCityForUserFromUpdate(update)));
                    break;
                }
                case RUB: {
                    bot.sendMsg(update, dtu.getRub(bot.getCityForUserFromUpdate(update)));
                    break;
                }
                case BTC: {
                    bot.sendMsg(update, dtu.getBTC());
                    break;
                }
                case OTHER: {
                    bot.sendMsg(update, dtu.getOtherCurrencyFirstHalf());
                    bot.sendMsg(update, dtu.getOtherCurrencySecondHalf());
                    break;
                }
                case CALC: {
                    bot.setCalcOn(update);
                    bot.execute(KeyboardSupplier.getCalcKeyboard(update));
                    break;
                }
                default: {
                    bot.sendMsg(update, KeyboardSupplier.noCurrency);
                    break;
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
        users = propsMessage.getProperty("users");
        requests = propsMessage.getProperty("requests");
    }
}
