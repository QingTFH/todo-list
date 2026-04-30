package main;

import token.dataToken.TodoToken;

import java.time.LocalDateTime;

public class Config {

    public static final boolean debug = false;
    public static LocalDateTime invalidTime =
            LocalDateTime.of(1999, 1, 1, 1, 1);

    public static TodoToken invalidToken = new TodoToken("", invalidTime);


}
