package util.handler;

import exception.InputException;
import manager.TodoManager;
import token.command.Command;
import util.TimeUtil;

public class EditHandler implements Handler {
    // -n num 待编辑序号(1基)，也支持裸参数 num
    // -c 新内容（可选，缺省则保留原内容）
    // -d/-h 新 ddl（可选，缺省则保留原 ddl，时间格式与 add 一致）

    @Override
    public void handle(Command cmd) {
        int index = Handler.indexOf(cmd, "edit");

        String content = cmd.getOption("c");
        String date = cmd.getOption("d");
        String hour = cmd.getOption("h");

        if(content != null && content.isEmpty()) {
            throw new InputException("edit -c 内容不能为空");
        }
        if(content == null && date == null && hour == null) {
            throw new InputException("edit至少需要修改内容(-c)或ddl(-d/-h)");
        }

        TodoManager.getInstance().edit(
                index,
                content,
                date != null || hour != null ? TimeUtil.parseTime(date, hour) : null);
    }
}
