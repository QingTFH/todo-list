package util.handler;

import manager.TodoManager;
import token.command.Command;

public class FinishHandler implements Handler {
    // -n num 完成第n个(索引为n-1)，也支持裸参数 num
    // -o 一键完成所有已逾期任务

    @Override
    public void handle(Command cmd) {
        if(cmd.getOption("o") != null) {
            TodoManager.getInstance().finishOverdue();
            return;
        }
        TodoManager.getInstance().finish(Handler.indexOf(cmd, "finish"));
    }
}
