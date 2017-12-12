package com.bot.updaters;

import com.bot.currencies.Market;
import com.bot.enums.Commands;
import com.bot.enums.MarketType;

import java.util.List;
import java.util.Map;


public interface Updater {
    Map<Commands, Market> sendRequest(MarketType request);
}
