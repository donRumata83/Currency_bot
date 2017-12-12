package com.bot.updaters;

import com.bot.currencies.Market;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;
import com.bot.updaters.Updater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FakeUpdater implements Updater {
    @Override
    public Map<Commands, Market> sendRequest(MarketType request) {

        return Collections.emptyMap();
    }
}
