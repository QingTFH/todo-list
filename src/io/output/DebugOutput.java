package io.output;

import main.Config;
import util.ErrorUtil;

public class DebugOutput {

    public static void debugPrint(String out) {
        if(Config.debug) {
            ErrorUtil.wrongHappen(out);
        }
    }

}
