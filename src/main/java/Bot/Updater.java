package Bot;

import Bot.Enums.MarketType;

import java.util.List;


interface Updater {
    List<Float> sendRequest(MarketType request);
}
