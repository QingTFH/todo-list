package controller;

import exception.WrongException;
import manager.TodoManager;
import token.command.Command;
import util.handler.AddHandler;
import util.handler.DeleteHandler;
import util.handler.EditHandler;
import util.handler.FinishHandler;
import util.handler.Handler;
import util.handler.HelpHandler;
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
        handlers.put(Command.Operator.edit, new EditHandler());
        handlers.put(Command.Operator.delete, new DeleteHandler());
        handlers.put(Command.Operator.help, new HelpHandler());
    }

    public void run(Command command) {
        if(command.getCommandType() == Command.Operator.stop) {
            return;
        }

        dispatch(command);
    }

    public void end() {
        manager.saveAll();
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
