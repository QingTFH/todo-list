package util.handler;

import exception.InputException;
import manager.TodoManager;
import token.command.Command;

public class QueryHandler implements Handler {
    // -a 所有
    // -n num 查询前n个
    // -f 查询已完成事项（默认前10条；-a 全部；-n num 前num条）
    // 详情(--detail) 额外展示 pri 与 score

    @Override
    public void handle(Command cmd) {
        TodoManager manager = TodoManager.getInstance();
        // 支持 query 详情 / query -详情 / query --detail 三种写法
        boolean detail = cmd.getOption("详情") != null || cmd.getOthers().contains("详情");

        if(cmd.getOption("f") != null) {
            handleFinished(manager, cmd, detail);
            return;
        }

        String n = cmd.getOption("n");
        boolean a = cmd.getOption("a") != null;

        if(a || n == null) {
            manager.query(detail);
        } else {
            try {
                manager.query(Integer.parseInt(n), detail);
            } catch (NumberFormatException e) {
                throw new InputException("指令query -n i中, i不是数字");
            }
        }
    }

    private void handleFinished(TodoManager manager, Command cmd, boolean detail) {
        String n = cmd.getOption("n");
        boolean a = cmd.getOption("a") != null;

        if(a) {
            manager.queryFinishedAll(detail);
        } else if(n == null) {
            manager.queryFinished(detail);
        } else {
            try {
                manager.queryFinished(Integer.parseInt(n), detail);
            } catch (NumberFormatException e) {
                throw new InputException("指令query -f -n i中, i不是数字");
            }
        }
    }
}
