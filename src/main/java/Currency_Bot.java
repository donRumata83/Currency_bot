import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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

    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }
}
