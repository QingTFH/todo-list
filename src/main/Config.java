package main;

import token.dataToken.TodoToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Config {

    public static final boolean debug = false;

    /* util */
    // todoUtil
    public static final String FILE_PATH = "todo.txt"; // 文件路径
    // timeUtil
    public static String allMatter = "yyyy-MM-dd HH:mm";
    public static String dateMatter = "yyyy-MM-dd";
    public static String hourMatter = "HH:mm";
    public static DateTimeFormatter all_formatter = DateTimeFormatter.ofPattern(allMatter);
    public static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern(dateMatter);
    public static DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern(hourMatter);
    public static final LocalDateTime invalidTime =
            LocalDateTime.of(1999, 1, 1, 1, 1);
    public static final TodoToken invalidToken = new TodoToken("", invalidTime);

    /* io */
    // input
    public static final String CLI_PROMPT = "> ";
    // output
    public static final String FIRST_PRINT = "输入示例: add -m content -d 2025-04-29 -h 17:00";

}
