package controller;

import exception.WrongException;
import manager.TodoManager;
import token.command.Command;
import util.handler.AddHandler;
import util.handler.FinishHandler;
import util.handler.Handler;
import util.handler.QueryHandler;

import java.util.HashMap;


public class Controller {
    // 调度员

    TodoManager manager = TodoManager.getInstance();
    HashMap<Command.Operator, Handler> handlers = new HashMap<>();

    public Controller() {
        handlers.put(Command.Operator.add, new AddHandler());
        handlers.put(Command.Operator.query, new QueryHandler());
        handlers.put(Command.Operator.finish, new FinishHandler());
    }

    public void run(Command command) {
        if(command == Command.POISON
                || command.getCommandType() == Command.Operator.stop) {
            return;
        }

        dispatch(command);
    }

    public void end() {
        manager.save();
    }

    private void dispatch(Command command) {
        Command.Operator operator = command.getCommandType();
        Handler handler = handlers.get(operator);
        if(handler == null) {
            throw new WrongException("Illegal Command operator");
        }
        handler.handle(command);
    }

}
