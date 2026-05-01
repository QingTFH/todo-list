package io.output;

import main.Config;

public class DebugOutput {

    public static void debugPrint(String out) {
        if(Config.debug) {
            System.out.println("[Debug] " + out);
        }
    }

}
