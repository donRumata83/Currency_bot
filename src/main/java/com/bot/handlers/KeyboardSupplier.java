package com.bot.handlers;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KeyboardSupplier {
    private static String newCurrencyRequestMessage;
    private static String other;
    private static String usd;
    private static String eur;
    private static String rub;
    private static String btc;

    private static String calc;

    private static String sellUsd;
    private static String buyUsd;
    private static String sellEur;
    private static String buyEur;
    private static String sellRub;
    private static String buyRub;
    private static String exit;

    public KeyboardSupplier() {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static SendMessage getStandartKeyboard(Message message) {
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
        return keybordMessage;
    }

    public static SendMessage getCalcKeybord(Update update) {
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
        return keybordMessage;
    }

    private void loadProperties() throws IOException {
        Properties propsMessage = new Properties();
        InputStream in = getClass().getResourceAsStream("/message.properties");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8.name()));
        propsMessage.load(reader);
        usd = propsMessage.getProperty("usd");
        eur = propsMessage.getProperty("eur");
        rub = propsMessage.getProperty("rub");
        btc = propsMessage.getProperty("btc");
        newCurrencyRequestMessage = propsMessage.getProperty("newreq");
        other = propsMessage.getProperty("other");
        calc = propsMessage.getProperty("calc");
        calc = propsMessage.getProperty("calc");
        sellUsd = propsMessage.getProperty("sellUsd");
        buyUsd = propsMessage.getProperty("buyUsd");
        sellEur = propsMessage.getProperty("sellEur");
        buyEur = propsMessage.getProperty("buyEur");
        sellRub = propsMessage.getProperty("sellRub");
        buyRub = propsMessage.getProperty("buyRub");
        exit = propsMessage.getProperty("exit");
    }
}
