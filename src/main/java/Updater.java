import Currencies.Currency;
import Enums.Commands;

import java.util.HashMap;

interface Updater {
    HashMap<Commands, Currency> sendRequest(String request);

}
