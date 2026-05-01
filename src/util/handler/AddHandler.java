package util.handler;

import manager.TodoManager;
import token.command.Command;
import util.TodoTokenFactory;


public class AddHandler implements Handler {
    // -m message
    // -d time_date
    // -h time_hour
    // 根据command里的内容和以上选项，构建一个todoToken,加入todoList中


    public AddHandler() {
    }

    public void handle(Command cmd) {
        String message = cmd.getOption("m");
        String date = cmd.getOption("d");
        String hour = cmd.getOption("h");

        TodoManager.getInstance().add(TodoTokenFactory.newToken(message, date, hour));
    }

}
