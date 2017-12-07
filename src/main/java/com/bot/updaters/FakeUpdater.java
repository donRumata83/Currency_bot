package com.bot.updaters;

import com.bot.enums.MarketType;
import com.bot.updaters.Updater;

import java.util.ArrayList;

public class FakeUpdater implements Updater {
    @Override
    public ArrayList<Float> sendRequest(MarketType request) {
        ArrayList<Float> result = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            result.add(8.0F);
        }
        return result;
    }
}
