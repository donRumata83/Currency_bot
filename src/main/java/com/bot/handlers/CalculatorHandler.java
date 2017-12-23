package com.bot.handlers;

import com.bot.CurrencyBot;
import com.bot.DataTransformerUtil;
import com.bot.currencies.Currency;
import com.bot.enums.CalcCommands;
import com.bot.enums.Commands;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class CalculatorHandler implements UpdateHandler {
    private CurrencyBot bot;
    private DataTransformerUtil dtu;
    private CalcCommands command = CalcCommands.DEF;

    private static String notNumber;
    private static String enterSum;
    private static String betterCurse;
    private static String sumLayout;
    private static String newSearch;
    private static String requests;

    public CalculatorHandler(CurrencyBot bot, DataTransformerUtil dtu) {
        this.bot = bot;
        this.dtu = dtu;
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Update update) throws TelegramApiException {
        Message message = update.getMessage();
        int sum;
        if (update.hasMessage() && message.hasText()) {
            String message_text = update.getMessage().getText();
            try {
                sum = Integer.parseInt(message_text);
                if (sum < 1000) {
                    bot.sendMsg(update, getSum(sum, command, update));
                    bot.sendMsg(update, newSearch);
                } else {
                    bot.sendMsg(update, getSum(sum, command, update));
                    bot.sendMsg(update, newSearch);
                    //bot.sendMsg(message, betterCurse);
                }
            } catch (NumberFormatException e) {
                try {
                    switch (message_text) {
                        case "/new": {
                            bot.setCalcOff(update);
                            KeyboardSupplier.getStandartKeyboard(update);
                            break;
                        }
                        case "/mstat": {
                            bot.sendMsg(update, requests + bot.messageCounter);
                            bot.sendMsg(update, newSearch);
                            break;
                        }
                        case "/start":
                            bot.execute(KeyboardSupplier.getCityKeyboard(update));
                            bot.removeCity(update);
                            break;
                        default: {
                            if (message_text.equals(KeyboardSupplier.exit) || message_text.equals("/new")) {
                                bot.setCalcOff(update);
                                bot.execute(KeyboardSupplier.getStandartKeyboard(update));
                            } else {
                                bot.sendMsg(update, enterSum);
                                command = CommandsSupplier.getCalcCommand(message_text);
                            }
                            break;
                        }
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    bot.sendMsg(update, newSearch);
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
        newSearch = propsMessage.getProperty("new");
        requests = propsMessage.getProperty("requests");
        newSearch = propsMessage.getProperty("new");
        enterSum = propsMessage.getProperty("enterSum");
        notNumber = propsMessage.getProperty("notNumber");
        betterCurse = propsMessage.getProperty("betterCurse");
        sumLayout = propsMessage.getProperty("sumLayout");
    }

    private String getSum(int count, CalcCommands command, Update update) {
        Float result = 0.0f;
        Map<Commands, Currency> map = dtu.getMap();
        switch (command) {
            case SELL_USD:
                result = count * map.get(Commands.USD).getAuc_ask(bot.getCityForUserFromUpdate(update));
                break;
            case BUY_USD:
                result = count * map.get(Commands.USD).getAuc_bid(bot.getCityForUserFromUpdate(update));
                break;
            case SELL_EUR:
                result = count * map.get(Commands.EURO).getAuc_ask(bot.getCityForUserFromUpdate(update));
                break;
            case BUY_EUR:
                result = count * map.get(Commands.EURO).getAuc_bid(bot.getCityForUserFromUpdate(update));
                break;
            case SELL_RUB:
                result = count * map.get(Commands.RUB).getAuc_ask(bot.getCityForUserFromUpdate(update));
                break;
            case BUY_RUB:
                result = count * map.get(Commands.RUB).getAuc_bid(bot.getCityForUserFromUpdate(update));
                break;
        }
        return String.format(sumLayout, result);
    }


}

