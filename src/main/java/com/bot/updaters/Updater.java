package com.bot.updaters;

import com.bot.enums.MarketType;

import java.util.List;


public interface Updater {
    List<Float> sendRequest(MarketType request);
}
