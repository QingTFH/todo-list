package io.output;

import main.Config;

import java.time.Duration;
import java.time.LocalDateTime;

public class Output {

    public static void printWelcome() {
        Duration duration = Duration.between(Config.START_TIME, LocalDateTime.now());
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        System.out.println("星海系统持续陪伴您，目前已航行" + days + "日" + hours + "时");
    }

    public static void print(String out) {
        System.out.println(out);
    }

}
