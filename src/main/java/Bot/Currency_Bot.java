package Bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import Bot.Enums.Commands;



import java.io.IOException;
import java.util.Properties;

/**
 * Main class
 */
public class Currency_Bot extends TelegramLongPollingBot {
    private String token;
    private String botName;
    private Data_transformer_Util data_transformer_util;
    private static long counter = 0;
    private static long mcounter = 0;

    private final String helpComand = "Это бот для получения актуальных курсов валют" + "\n" +
            "Вы можете использовать такие команды:" + "\n" +
            "   /USD - для получения курса доллара" + "\n" +
            "   /EURO - для получения курса евро" + "\n" +
            "   /RUB - для получения курса рубля" + "\n" +
            "Также возможно использования названий валют как на русском так и английском языках" + "\n"
            + "Курсы валют актуальны благодаря ресурсу minfin.com.ua/currency";

    private final String greetingCommand = "Добрый день, я бот который знает все о валютах. " +
            "Курс какой валюты вы хотели бы узнать?";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        Currency_DB currency_db = new Currency_DB(new MinfinUpdater());
        try {
            botapi.registerBot(new Currency_Bot(new Data_transformer_Util(currency_db)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructor
     *
     * @param data_transformer_util
     */
    private Currency_Bot(Data_transformer_Util data_transformer_util) {
        this.data_transformer_util = data_transformer_util;
        Properties props = new Properties();
        try {
            props.load(Currency_Bot.class.getResourceAsStream("/config.properties"));
            this.token = props.getProperty("token");
            this.botName = props.getProperty("botName");
        } catch (IOException e) {
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
    private void sendMsg(Message message, String text) {
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
    private String messageCheck(Message message) {
        switch (Commands.convert(message.getText().trim().toUpperCase())) {
            case START: {
                counter++;
                return greetingCommand;
            }
            case HELP:
                return helpComand;
            case USD: {
                mcounter++;
                return data_transformer_util.getUSD();
            }
            case EURO: {
                mcounter++;
                return data_transformer_util.getEuro();
            }
            case RUB: {
                mcounter++;
                return data_transformer_util.getRub();
            }
            case STAT:
                return "Юзеров: " + counter;
            case MSTAT:
                return "Запросов: " + mcounter;
            default:
                return "Извините не могу ответить на это сообщение, попробуйте еще раз.";
        }
    }
}
