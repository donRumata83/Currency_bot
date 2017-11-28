import Currencies.Currency;
import Enums.Commands;
import Enums.Market_Type;

import java.util.HashMap;

interface Updater {
    HashMap<Commands, Currency> sendRequest(Market_Type request);

}
