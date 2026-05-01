package util.handler;

import exception.InputException;
import manager.TodoManager;
import token.command.Command;

public class FinishHandler implements Handler{
    // -n num 完成第n个(索引为n-1)

    @Override
    public void handle(Command cmd) {

        String n = cmd.getOption("n");

        if(n == null) {
            if (cmd.getOthers().isEmpty()) {
                throw new InputException("finish无参数");
            }
            n = cmd.getOthers().get(0);
        }

        try {
            TodoManager.getInstance().finish(Integer.parseInt(n) - 1);
        } catch (NumberFormatException e) {
            throw new InputException("finish参数错误");
        }
    }
}
