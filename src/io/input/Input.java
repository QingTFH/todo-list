package io.input;

import controller.Controller;
import exception.InputException;
import exception.LoadSaveException;
import io.output.DebugOutput;
import io.output.Output;
import token.command.Command;

import java.io.InputStream;
import java.util.Scanner;

import static main.Config.CLI_PROMPT;

public class Input {

    private final Scanner scanner;
    private final Controller controller;

    public Input(InputStream input) {
        scanner = new Scanner(input);
        controller = new Controller();
    }

    public void run() {

        while (true) {
            try {
                System.out.print(CLI_PROMPT);
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                Command cmd = Command.Parser.parseCommand(line);
                if (cmd == null) { // 空指令
                    continue;
                }

                if (cmd.getCommandType() == Command.Operator.stop) {
                    break;
                }

                controller.run(cmd);
            } catch (InputException e) {
                e.print();
            }
        }

        try {
            end();
            DebugOutput.debugPrint("退出input");
            Output.print("退出程序");
        } catch (LoadSaveException w) {
            w.print();
        }
    }

    private void end() {
        controller.end();
    }

}
