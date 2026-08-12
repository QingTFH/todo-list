package util.handler;

import manager.TodoManager;
import token.command.Command;

public class UndoHandler implements Handler {
    // undo 撤销上一步修改（add/finish/delete/edit/批量逾期）

    @Override
    public void handle(Command cmd) {
        TodoManager.getInstance().undo();
    }
}
