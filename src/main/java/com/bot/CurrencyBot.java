package com.bot;

import com.bot.currencies.Currency;
import com.bot.enums.CalcCommands;
import com.bot.enums.Commands;
import com.bot.updaters.BitcoinUpdater;
import com.bot.updaters.MinfinUpdater;
import com.bot.updaters.NBUUpdater;
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
import java.util.*;

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
    private static String other;
    private static String usd;
    private static String eur;
    private static String rub;
    private static String btc;

    private static String notNumber;
    private static String enterSum;
    private static String betterCurse;

    private static String calc;
    private static String sellUsd;
    private static String buyUsd;
    private static String sellEur;
    private static String buyEur;
    private static String sellRub;
    private static String buyRub;
    private static String sumLayout;
    private static String exit;

    private StandartMessageHandler standartMessageHandler;
    private CalcMessageListener calcMessageHandler;
    private boolean isCalcOn = false;


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        CurrencyDB currency_db = new CurrencyDB(new MinfinUpdater(), new BitcoinUpdater(), new NBUUpdater());
        CurrencyBot bot = new CurrencyBot(new DataTransformerUtil(currency_db));
        try {
            botsApi.registerBot(bot);
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
        loadProperties();
        this.standartMessageHandler = new StandartMessageHandler();
        this.calcMessageHandler = new CalcMessageListener();
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

    /**
     * Gets the update from user chat, check for not empty and send text message to user
     *
     * @param update from user chat
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (!isCalcOn) standartMessageHandler.handle(update);
        else calcMessageHandler.handle(update);
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
            sellUsd = propsMessage.getProperty("sellUsd");
            buyUsd = propsMessage.getProperty("buyUsd");
            sellEur = propsMessage.getProperty("sellEur");
            buyEur = propsMessage.getProperty("buyEur");
            sellRub = propsMessage.getProperty("sellRub");
            buyRub = propsMessage.getProperty("buyRub");
            exit = propsMessage.getProperty("exit");
            enterSum = propsMessage.getProperty("enterSum");
            notNumber = propsMessage.getProperty("notNumber");
            betterCurse = propsMessage.getProperty("betterCurse");
            sumLayout=propsMessage.getProperty("sumLayout");
            other=propsMessage.getProperty("other");
        } catch (Exception e) {
            e.printStackTrace();
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
        rows.add(Collections.singletonList(new InlineKeyboardButton().setText(other).setCallbackData("other")));
        rows.add(Collections.singletonList(new InlineKeyboardButton().setText(calc).setCallbackData("calc")));
        markupInline.setKeyboard(rows);
        keybordMessage.setReplyMarkup(markupInline);
        try {
            execute(keybordMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getCalcKeybord(Update update) {
        SendMessage keybordMessage = new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setText(calc);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstLine = new ArrayList<>();
        firstLine.add(new InlineKeyboardButton().setText(sellUsd).setCallbackData("sellUsd"));
        firstLine.add(new InlineKeyboardButton().setText(buyUsd).setCallbackData("buyUsd"));
        List<InlineKeyboardButton> secondLine = new ArrayList<>();
        secondLine.add(new InlineKeyboardButton().setText(sellEur).setCallbackData("sellEur"));
        secondLine.add(new InlineKeyboardButton().setText(buyEur).setCallbackData("buyEur"));
        List<InlineKeyboardButton> thirdLine = new ArrayList<>();
        thirdLine.add(new InlineKeyboardButton().setText(sellRub).setCallbackData("sellRub"));
        thirdLine.add(new InlineKeyboardButton().setText(buyRub).setCallbackData("buyRub"));
        rows.add(firstLine);
        rows.add(secondLine);
        rows.add(thirdLine);
        rows.add(Collections.singletonList(new InlineKeyboardButton().setText(exit).setCallbackData("exit")));
        markupInline.setKeyboard(rows);
        keybordMessage.setReplyMarkup(markupInline);
        try {
            execute(keybordMessage); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private class StandartMessageHandler {
        void handle(Update update) {
            Message message = update.getMessage();
            if (update.hasMessage() && message.hasText()) {
                String message_text = update.getMessage().getText();
                switch (message_text) {
                    case "/start": {
                        counter++;
                        sendMsg(message, String.format(greetingCommand, message.getFrom().getFirstName()));

                        getStandartKeyboard(message);
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
                        isCalcOn = false;
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
                        case "other": {
                            sendMessageWithQuery(update, dataTransformer_util.getOtherCurrencyFirstHalf());
                            sendMessageWithQuery(update, dataTransformer_util.getOtherCurrencySecondHalf());
                            sendMessageWithQuery(update, newSearch);
                            break;
                        }
                        case "calc": {
                            isCalcOn = true;
                            getCalcKeybord(update);
                            break;
                        }
                    }
                }
            }
        }
    }

    private class CalcMessageListener {
        CalcCommands command = CalcCommands.DEF;

        void handle(Update update) {
            Message message = update.getMessage();
            int sum;
            if (update.hasMessage() && message.hasText()) {
                String message_text = update.getMessage().getText();
                try {
                    sum = Integer.parseInt(message_text);
                    if (sum < 1000) {
                        sendMsg(message, getSum(sum, command));
                        sendMsg(message, newSearch);
                    } else {
                        sendMsg(message, getSum(sum, command));
                        sendMsg(message, newSearch);
                        sendMsg(message, betterCurse);
                    }
                } catch (NumberFormatException e) {
                    try {switch (message_text) {
                        case "/new": {
                            isCalcOn = false;
                            getStandartKeyboard(message);
                            break;
                        }
                        case "/mstat": {
                            sendMessageWithQuery(update, requests + messageCounter);
                            sendMessageWithQuery(update, newSearch);
                            break;
                        }
                        default: {
                            sendMessageWithQuery(update, notNumber);
                            break;
                        }
                    }} catch (NullPointerException ex) {
                        ex.printStackTrace();
                        sendMsg(message, newSearch);
                    }

                }
            } else {
                if (update.hasCallbackQuery()) {
                    String data = update.getCallbackQuery().getData();
                    switch (data) {
                        case "sellUsd": {
                            sendMessageWithQuery(update, enterSum);
                            command = CalcCommands.SELL_USD;
                            break;
                        }
                        case "buyUsd": {
                            sendMessageWithQuery(update, enterSum);
                            command = CalcCommands.BUY_USD;
                            break;
                        }
                        case "sellEur": {
                            sendMessageWithQuery(update, enterSum);
                            command = CalcCommands.SELL_EUR;
                            break;
                        }
                        case "buyEur": {
                            sendMessageWithQuery(update, enterSum);
                            command = CalcCommands.BUY_EUR;
                            break;
                        }
                        case "sellRub": {
                            sendMessageWithQuery(update, enterSum);
                            command = CalcCommands.SELL_RUB;
                            break;
                        }
                        case "buyRub": {
                            sendMessageWithQuery(update, enterSum);
                            command = CalcCommands.BUY_RUB;
                            break;
                        }
                        case "exit": {
                            isCalcOn = false;
                            sendMessageWithQuery(update, newSearch);
                            break;
                        }
                    }
                }
            }
        }

        private String getSum(int count, CalcCommands command) {
            Float result = 0.0f;
            Map<Commands, Currency> map = dataTransformer_util.getMap();
            switch (command) {
                case SELL_USD:
                    result = count * map.get(Commands.USD).getAuc_ask();
                    break;
                case BUY_USD:
                    result = count * map.get(Commands.USD).getAuc_bid();
                    break;
                case SELL_EUR:
                    result = count * map.get(Commands.EURO).getAuc_ask();
                    break;
                case BUY_EUR:
                    result = count * map.get(Commands.EURO).getAuc_bid();
                    break;
                case SELL_RUB:
                    result = count * map.get(Commands.RUB).getAuc_ask();
                    break;
                case BUY_RUB:
                    result = count * map.get(Commands.RUB).getAuc_bid();
                    break;
            }
            return String.format(sumLayout, result);
        }
    }
}
