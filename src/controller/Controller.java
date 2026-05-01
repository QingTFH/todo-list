package controller;

import exception.InputException;
import exception.LoadSaveException;
import exception.WrongException;
import manager.TodoManager;
import util.ErrorUtil;
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
                addTodo(command); // ls
            } break;
            case query : {
                queryTodo(command);
            } break;
            case finish: {
                finishTodo(command); // in ls
            } break;
            default: { // wrong
                throw new WrongException("Illegal Command operator");
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
        } catch (NumberFormatException e) {
            throw new InputException("finish 无参数");
        }
    }
}
