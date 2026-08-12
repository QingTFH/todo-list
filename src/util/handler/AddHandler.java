package util.handler;

import exception.InputException;
import main.Config;
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
        if(message == null || message.isEmpty()) {
            throw new InputException("add缺少内容, 用法: add -m 内容 [-d 日期] [-h 时间]");
        }
        String date = cmd.getOption("d");
        String hour = cmd.getOption("h");
        Integer importance = Handler.importanceOf(cmd, "add");

        TodoManager.getInstance().add(TodoTokenFactory.newToken(
                message, date, hour, importance != null ? importance : Config.DEFAULT_IMPORTANCE));
    }

}
