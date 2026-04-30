package controller;

import io.output.DebugOutput;
import io.output.Output;
import manager.TodoManager;
import util.TodoTokenFactory;
import token.command.Command;


public class Controller {
    // 调度员

    TodoManager manager = TodoManager.getInstance();

    public Controller() {}

    public void run(Command command) {
        if(command == Command.POISON
                || command.getCommandType() == Command.Operator.stop) {
            return;
        }
        dispatch(command);
    }

    public void end() {
        manager.save();
        System.out.println("存储todoList");
    }

    private void dispatch(Command command) {
        Command.Operator operator = command.getCommandType();
        switch (operator) {
            case add : {
                addTodo(command);
            } break;
            case query : {
                queryTodo(command);
            } break;
            case finish: {
                finishTodo(command);
            } break;
            default: {
                DebugOutput.debugPrint("非法operator");
            }
        }
    }

    private void addTodo(Command cmd) {
        // -m message
        // -d time_date
        // -h time_hour
        // 根据command里的内容和以上选项，构建一个todoToken,加入todoList中

        String message = cmd.getOption("m");
        String date = cmd.getOption("d");
        String hour = cmd.getOption("h");

        manager.add(TodoTokenFactory.newToken(message, date, hour));
    }

    private void queryTodo(Command cmd) {
        // -a 所有
        // -n num 查询前n个
        String n = cmd.getOption("n");
        if(n == null) {
            manager.query();
        } else {
            manager.query(Integer.parseInt(n));
        }
    }

    private void finishTodo(Command cmd) {
        // -n num 完成第n个(索引为n-1)
        String n = cmd.getOption("n");
        int num;

        if(n == null && !cmd.getOthers().isEmpty()) { // 没有option的内容, 并且others里有内容
            n = cmd.getOthers().get(0); // 试试看others里的行不行
        }
        try {
            num = Integer.parseInt(n);
            manager.finish(num - 1);
        } catch (NumberFormatException ignored) { // 不能被parseInt
            Output.print("finish因\"无参数\"而无效");
        }
    }
}
