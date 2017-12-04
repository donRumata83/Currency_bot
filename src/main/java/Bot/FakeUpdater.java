package Bot;

import Bot.Enums.MarketType;

import java.util.ArrayList;

public class FakeUpdater implements Updater{
    @Override
    public ArrayList<Float> sendRequest(MarketType request) {
        ArrayList<Float> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            result.add(1.6F);
        }
        return result;
    }
}
