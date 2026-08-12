package util.handler;

import manager.TodoManager;
import token.command.Command;

public class DeleteHandler implements Handler {
    // -n num 删除第n个(索引为n-1)，也支持裸参数 num

    @Override
    public void handle(Command cmd) {
        TodoManager.getInstance().delete(Handler.indexOf(cmd, "delete"));
    }
}
