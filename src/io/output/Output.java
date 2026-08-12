package io.output;

import main.Config;

import java.time.Duration;
import java.time.LocalDateTime;

public class Output {

    public static void printWelcome() {
        Duration duration = Duration.between(Config.START_TIME, LocalDateTime.now());
        long days = duration.toDays();
        System.out.println("星海系统苏醒，已航行" + days + "日");
    }

    public static void print(String out) {
        System.out.println(out);
    }

}
