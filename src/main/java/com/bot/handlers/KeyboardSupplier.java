package com.bot.handlers;

import com.bot.enums.City;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KeyboardSupplier {
    public static String newCurrencyRequestMessage;
    public static String other;
    public static String usd;
    public static String eur;
    public static String rub;
    public static String btc;

    public static String calc;
    private static String cities;

    public static String sellUsd;
    public static String buyUsd;
    public static String sellEur;
    public static String buyEur;
    public static String sellRub;
    public static String buyRub;
    public static String exit;
    public static String noCurrency;


    public KeyboardSupplier() {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SendMessage getStandartKeyboard(Update update) {

        SendMessage keybordMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(newCurrencyRequestMessage);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow first = new KeyboardRow();
        first.add(usd);
        first.add(eur);
        KeyboardRow second = new KeyboardRow();
        second.add(rub);
        second.add(btc);
        KeyboardRow third = new KeyboardRow();
        third.add(other);
        KeyboardRow fourth = new KeyboardRow();
        fourth.add(calc);
        List<KeyboardRow> rows = new ArrayList<>(Arrays.asList(first, second, third, fourth));
        /*InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

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
        markupInline.setKeyboard(rows);*/
        keyboardMarkup.setKeyboard(rows);
        keybordMessage.setReplyMarkup(keyboardMarkup);
        return keybordMessage;
    }

    public static SendMessage getCalcKeyboard(Update update) {
        SendMessage keybordMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(calc);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow first = new KeyboardRow();
        first.add(sellUsd);
        first.add(buyUsd);
        KeyboardRow second = new KeyboardRow();
        second.add(sellEur);
        second.add(buyEur);
        KeyboardRow third = new KeyboardRow();
        third.add(sellRub);
        third.add(buyRub);
        KeyboardRow fourth = new KeyboardRow();
        fourth.add(exit);
        List<KeyboardRow> rows = new ArrayList<>(Arrays.asList(first, second, third, fourth));
        keyboardMarkup.setKeyboard(rows);
        keybordMessage.setReplyMarkup(keyboardMarkup);

        /*InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
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
        keybordMessage.setReplyMarkup(markupInline);*/
        return keybordMessage;
    }

    public static SendMessage getCityKeyboard(Update update) {
        SendMessage keybordMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(cities);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow first = new KeyboardRow();
        first.add(City.KIEV.getName());
        first.add(City.KHARKOV.getName());
        first.add(City.ODESSA.getName());
        KeyboardRow second = new KeyboardRow();
        second.add(City.DNIPRO.getName());
        second.add(City.LVOV.getName());
        second.add(City.DONETSK.getName());
        KeyboardRow third = new KeyboardRow();
        third.add(City.ZAPOROZHYE.getName());
        third.add(City.NIKOLAEV.getName());
        third.add(City.VINNITSA.getName());
        KeyboardRow fourth = new KeyboardRow();
        fourth.add(City.POLTAVA.getName());
        fourth.add(City.KHMENTISKIY.getName());
        fourth.add(City.CHERNIGOV.getName());
        KeyboardRow fifth = new KeyboardRow();
        fifth.add(City.SUMY.getName());
        fifth.add(City.MARIUPOL.getName());
        fifth.add(City.ZHITOMIR.getName());
        KeyboardRow sixth = new KeyboardRow();
        sixth.add(City.DEFAULT.getName());
        List<KeyboardRow> rows = new ArrayList<>(Arrays.asList(first, second, third, fourth, fifth, sixth));
        keyboardMarkup.setKeyboard(rows);
        keybordMessage.setReplyMarkup(keyboardMarkup);
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
        cities = propsMessage.getProperty("cities");
        noCurrency = propsMessage.getProperty("nocurrency");
    }


}
