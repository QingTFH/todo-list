package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Config {

    public static final boolean debug = true;

    /* util */
    // timeUtil
    public static final String FILE_PATH = "todo.txt"; // 待办文件路径
    public static final String FINISH_FILE_PATH = "finish.txt"; // 已完成事项文件路径
    public static final int DEFAULT_FINISHED_QUERY_LIMIT = 10; // query -f 默认展示条数
    public static final String ALL_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String HOUR_PATTERN = "HH:mm";
    public static final DateTimeFormatter ALL_FORMATTER = DateTimeFormatter.ofPattern(ALL_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern(HOUR_PATTERN);
    public static final LocalDateTime INVALID_TIME =
            LocalDateTime.of(1999, 1, 1, 1, 1);

    /* io */
    // input
    public static final String CLI_PROMPT = "> ";
    // output
    public static final String FIRST_PRINT = "输入示例: add -m content -d 2025-04-29 -h 17:00";
}
