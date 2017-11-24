import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import Enums.Commands;

public class Currency_Bot extends TelegramLongPollingBot {
    private static final String TOKEN = "398060505:AAH3ZDikwgbPuiyTSgsE9ppaaiwRBgul6ao";
    private static final String BOT_NAME = "UACurrencyBot";
    private Data_transformer_Util data_transformer_util;
    private static long counter = 0;
    private static long mcounter = 0;

    private String helpComand = "\"Это бот для получения актуальных курсов валют\" +\n" +
            "                    \"Вы можете использовать такие команды:\" +\n" +
            "                    \"/USD - для получения курса доллара\" +\n" +
            "                    \"/EURO - для получения курса евро\" +\n" +
            "                    \"/RUB - для получения курса рубля\" +\n" +
            "                    \"/GBP - для получения курса фунта стерлингов\";";

    private String greetingCommand = "Добрый день, я бот который знает все о валютах. " +
            "Курс какой валюты вы хотели бы узнать?";

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        Currency_DB updater = new Currency_DB();
        try {
            botapi.registerBot(new Currency_Bot(new Data_transformer_Util(updater)));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private Currency_Bot(Data_transformer_Util data_transformer_util) {
        this.data_transformer_util = data_transformer_util;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            sendMsg(message, messageCheck(message));
        }

    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

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
                return data_transformer_util.getUSD();}
            case EURO: {
                mcounter++;
                return data_transformer_util.getEuro();}
            case RUB: {
                mcounter++;
                return data_transformer_util.getRub();}
            case GBP: {
                mcounter++;
                return data_transformer_util.getGbp();}
            case STAT:
                return "Юзеров: " + counter;
            case MSTAT: return  "Запросов: " + mcounter;
            default:
                return "Неизвестная валюта.";
        }
    }
}
