package io.input;

import main.Controller;
import token.command.Command;

import java.io.InputStream;
import java.util.Scanner;

public class Input {

    private final Scanner scanner;
    private final Controller controller;

    public Input(InputStream input) {
        scanner = new Scanner(input);
        controller = new Controller();
    }

    public void run() {
        try {
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                Command cmd = Command.Parser.parseCommand(line);
                if(cmd == null) { // 空指令
                    continue;
                }

                if(cmd.getCommandType() == Command.Operator.stop) {
                    break;
                }

                controller.run(cmd);
            }
        } finally {
            end();
            System.out.println("退出input");
        }
    }

    private void end() {
        controller.end();
    }

}
