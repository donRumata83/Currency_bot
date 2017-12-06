package bot;

import bot.enums.MarketType;

import java.util.List;


interface Updater {
    List<Float> sendRequest(MarketType request);
}
