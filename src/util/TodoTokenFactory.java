package util;

import token.dataToken.TodoToken;

import java.time.LocalDateTime;

public class TodoTokenFactory {

    public static TodoToken newToken(String content, String date, String hour) {
        return new TodoToken(content, TimeUtil.parseTime(date, hour));
    }

    public static TodoToken newToken(String content, LocalDateTime time) {
        return new TodoToken(content, time);
    }
}
