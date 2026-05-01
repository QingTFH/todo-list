package util;
import exception.InputException;
import token.dataToken.TodoToken;

import java.io.*;

public class TodoUtil {

    public static TodoToken parseTodoToken(String todoToken) {
        // ("ddl:" + deadline.format(Config.formatter) + "; content: " + content)
        int ddlBegin = todoToken.indexOf("ddl:") + "ddl:".length();
        int ddlEnd = todoToken.indexOf("; content: ");
        int contentBegin = todoToken.indexOf("; content: ") + "; content: ".length();
        int contentEnd = todoToken.length();

        String ddl = todoToken.substring(ddlBegin, ddlEnd);
        String content = todoToken.substring(contentBegin, contentEnd);

        return TodoTokenFactory.newToken(content, TimeUtil.parseTime(ddl));
    }


}
