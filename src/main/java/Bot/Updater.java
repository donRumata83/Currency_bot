package Bot;

import Bot.Enums.Market_Type;

import java.util.ArrayList;


interface Updater {
    ArrayList<Float> sendRequest(Market_Type request);

}
