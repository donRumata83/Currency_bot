import Currencies.*;
import Enums.Commands;

import java.util.HashMap;

public class Currency_DB {
    private HashMap<Commands, Currency> map = new HashMap<>();





    public HashMap<Commands, Currency> getMap() {
        return map;
    }
}
