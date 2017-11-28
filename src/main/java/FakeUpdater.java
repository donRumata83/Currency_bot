import Enums.Market_Type;

import java.util.ArrayList;

public class FakeUpdater implements Updater{
    @Override
    public ArrayList<Float> sendRequest(Market_Type request) {
        ArrayList<Float> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            result.add(1.6F);
        }
        return result;
    }
}
