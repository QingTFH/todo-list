package token;

import main.Config;

import java.time.LocalDateTime;
import java.util.Objects;

public class TodoTokenFactory {

    public static LocalDateTime invalidTime() {
        return LocalDateTime.of(1999, 1, 1, 1, 1);
    }

    public static TodoToken invalidToken() {
        return new TodoToken("", invalidTime());
    }

    public static TodoToken newToken(String content, String date, String hour) {
        return new TodoToken(content, Config.parseTime(date, hour));
    }

    public static TodoToken newToken(String content, LocalDateTime time) {
        return new TodoToken(content, time);
    }
}
