package util.handler;

import exception.InputException;
import manager.TodoManager;
import token.command.Command;

public class QueryHandler implements Handler {
    // -a 所有
    // -n num 查询前n个
    // -f 查询已完成事项（默认前10条；-a 全部；-n num 前num条）

    @Override
    public void handle(Command cmd) {
        TodoManager manager = TodoManager.getInstance();

        if(cmd.getOption("f") != null) {
            handleFinished(manager, cmd);
            return;
        }

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

    private void handleFinished(TodoManager manager, Command cmd) {
        String n = cmd.getOption("n");
        boolean a = cmd.getOption("a") != null;

        if(a) {
            manager.queryFinishedAll();
        } else if(n == null) {
            manager.queryFinished();
        } else {
            try {
                manager.queryFinished(Integer.parseInt(n));
            } catch (NumberFormatException e) {
                throw new InputException("指令query -f -n i中, i不是数字");
            }
        }
    }
}
