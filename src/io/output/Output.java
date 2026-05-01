package io.output;

import main.Config;
import token.command.Command;

public class Output {

    public static void printFirst() {
        for (Command.Operator a : Command.Operator.values()) {
            System.out.print(a + ", ");
        }
        System.out.println();
        System.out.println(Config.FIRST_PRINT);
    }

    public static void print(String out) {
        System.out.println(out);
    }

}
