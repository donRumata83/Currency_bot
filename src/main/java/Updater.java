import Currencies.Currency;
import Enums.Commands;
import Enums.Market_Type;

import java.util.ArrayList;
import java.util.HashMap;

interface Updater {
    ArrayList<Long> sendRequest(Market_Type request);

}
