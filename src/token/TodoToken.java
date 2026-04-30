package token;

import main.Config;

import java.time.LocalDateTime;

public class TodoToken {

    private final String content;
    private final LocalDateTime deadline;

    public TodoToken(String content, LocalDateTime time) {
        this.content = content;
        this.deadline = time;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return ("ddl:" + deadline.format(Config.all_formatter) + "; content: " + content);
    }


}
