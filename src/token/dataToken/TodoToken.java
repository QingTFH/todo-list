package token.dataToken;

import main.Config;

import java.time.LocalDateTime;

public class TodoToken {

    private final String content;
    private final LocalDateTime deadline;
    private final int importance;

    public TodoToken(String content, LocalDateTime time) {
        this(content, time, Config.DEFAULT_IMPORTANCE);
    }

    public TodoToken(String content, LocalDateTime time, int importance) {
        this.content = content;
        this.deadline = time;
        this.importance = importance;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public int getImportance() {
        return importance;
    }

    @Override
    public String toString() {
        return ("pri:" + importance + "; ddl:" + deadline.format(Config.ALL_FORMATTER) + "; content: " + content);
    }

}
