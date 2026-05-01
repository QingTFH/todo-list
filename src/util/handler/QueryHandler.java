package util.handler;

import exception.InputException;
import manager.TodoManager;
import token.command.Command;

public class QueryHandler implements Handler {
    // -a 所有
    // -n num 查询前n个

    @Override
    public void handle(Command cmd) {
        TodoManager manager = TodoManager.getInstance();
        String n = cmd.getOption("n");
        boolean a = cmd.getOption("a") != null;

        if(a || n == null) {
            manager.query();
        } else {
            try {
                manager.query(Integer.parseInt(n));
            } catch (NumberFormatException e) {
                throw new InputException("指令query -n i中, i不是数字");
            }
        }
    }
}
