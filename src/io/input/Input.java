package io.input;

import controller.Controller;
import io.output.DebugOutput;
import io.output.Output;
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
            DebugOutput.debugPrint("退出input");
            Output.print("退出程序");
        }
    }

    private void end() {
        controller.end();
    }

}
