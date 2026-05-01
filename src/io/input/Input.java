package io.input;

import controller.Controller;
import exception.InputException;
import exception.LoadSaveException;
import exception.WrongException;
import io.output.DebugOutput;
import io.output.Output;
import token.command.Command;

import java.io.InputStream;
import java.util.Scanner;

public class Input {

    private final Scanner scanner;
    private final Controller controller;

    public Input(InputStream input) throws InputException, LoadSaveException { // 初始化失败干脆直接抛出
        scanner = new Scanner(input);
        controller = new Controller();
    }

    public void run() {

        while (true) {
            try {
                System.out.print("> ");
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

    private void end() throws LoadSaveException {
        controller.end();
    }

}
