package Bot;

import Bot.Enums.MarketType;

import java.util.ArrayList;


interface Updater {
    ArrayList<Float> sendRequest(MarketType request);

}
