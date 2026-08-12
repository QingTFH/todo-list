package util;

import exception.InputException;
import main.Config;
import token.dataToken.TodoToken;

public class TodoUtil {

    /** 解析一行 "pri:x; ddl:...; content:..."；缺省 pri 时按默认重要度 */
    public static TodoToken parseTodoToken(String line) {
        int importance = Config.DEFAULT_IMPORTANCE;
        if(line.startsWith("pri:")) {
            int priEnd = line.indexOf(';');
            try {
                importance = Integer.parseInt(line.substring("pri:".length(), priEnd).trim());
            } catch (NumberFormatException e) {
                throw new InputException("pri格式错误, 请检查文件: " + line);
            }
        }

        int ddlBegin = line.indexOf("ddl:") + "ddl:".length();
        int ddlEnd = line.indexOf("; content: ");
        String ddl = line.substring(ddlBegin, ddlEnd);
        String content = line.substring(ddlEnd + "; content: ".length());

        return TodoTokenFactory.newToken(content, TimeUtil.parseTime(ddl), importance);
    }

}
