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

    /* priority: 重要度与紧急度加权 */
    public static final int IMPORTANCE_MIN = 0;
    public static final int IMPORTANCE_MAX = 3;
    public static final int DEFAULT_IMPORTANCE = 1;
    public static final int IMPORTANCE_WEIGHT = 3; // score 中重要度的权重
    public static final int URGENCY_SCALE = 6;      // score 中紧迫度反比例常数
    public static final int CONTENT_COLUMN_CAP = 24; // query 中 content 列宽上限

    /* io */
    // input
    public static final String CLI_PROMPT = "> ";

    /* 启动语 */
    public static final LocalDateTime START_TIME = LocalDateTime.of(2026, 4, 27, 0, 0); // 星海系统起航时间
}
