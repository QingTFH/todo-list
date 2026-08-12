package util;

import main.Config;
import token.dataToken.TodoToken;

import java.time.LocalDateTime;

public class TodoTokenFactory {

    public static TodoToken newToken(String content, String date, String hour) {
        return newToken(content, date, hour, Config.DEFAULT_IMPORTANCE);
    }

    public static TodoToken newToken(String content, String date, String hour, int importance) {
        return new TodoToken(content, TimeUtil.parseTime(date, hour), importance);
    }

    public static TodoToken newToken(String content, LocalDateTime time) {
        return new TodoToken(content, time, Config.DEFAULT_IMPORTANCE);
    }

    public static TodoToken newToken(String content, LocalDateTime time, int importance) {
        return new TodoToken(content, time, importance);
    }
}
