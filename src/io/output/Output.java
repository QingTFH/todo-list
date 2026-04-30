package io.output;

import token.command.Command;

public class Output {

    public static void printFirst() {
        for (Command.Operator a : Command.Operator.values()) {
            System.out.print(a + ", ");
        }
        System.out.println();
        System.out.println("输入示例: add -m content -d 2025-04-29 -h 17:00");
    }

}
