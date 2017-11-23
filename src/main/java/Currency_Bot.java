import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import Enums.Currency_Type;

public class Currency_Bot extends TelegramLongPollingBot{
    private static final String TOKEN = "398060505:AAH3ZDikwgbPuiyTSgsE9ppaaiwRBgul6ao";
    private static final String BOT_NAME = "UACurrencyBot";
    private CurrencyDB currencyDB;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Currency_Bot(new CurrencyDB(new Currency_Updater())));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public Currency_Bot(CurrencyDB currencyDB) {
        this.currencyDB = currencyDB;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText().trim().toUpperCase()) {
                case Currency_Type.USD : sendMsg(message, currencyDB.getUSD());
                break;
                case Currency_Type.EURO: sendMsg(message, currencyDB.getEuro());
                break;
                case Currency_Type.RUB: sendMsg(message, currencyDB.getRub());
                break;
                case Currency_Type.GBP: sendMsg(message, currencyDB.getGbp());
                break;
                default: sendMsg(message, "Не знаю такой валюты.");
            }
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
}
