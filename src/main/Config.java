package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Config {

    public static String allMatter = "yyyy-MM-dd HH:mm";
    public static String dateMatter = "yyyy-MM-dd";
    public static String hourMatter = "HH:mm";

    public static DateTimeFormatter all_formatter = DateTimeFormatter.ofPattern(allMatter);
    public static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern(dateMatter);
    public static DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern(hourMatter);

    public static LocalDateTime parseTime(String time) {
        try {
            return LocalDateTime.parse(time, all_formatter);
        } catch (DateTimeParseException ignored) {}

        // 2. 再尝试【仅日期格式】→ 自动补 00:00
        try {
            LocalDate date = LocalDate.parse(time, date_formatter);
            return date.atStartOfDay();
        } catch (DateTimeParseException ignored) {}

        // 3. 最后尝试【仅时间格式】→ 自动补 今天的日期
        try {
            LocalTime localTime = LocalTime.parse(time, hour_formatter);
            return LocalDateTime.of(LocalDate.now(), localTime);
        } catch (DateTimeParseException ignored) {}

        // 所有格式都不匹配
        System.out.println("时间格式错误！");
        return null;
    }

    public static LocalDateTime parseTime(String date, String hour) {
        if(date == null || date.isEmpty()) {
            return parseTime(hour);
        }

        if(hour == null || hour.isEmpty()) {
            return parseTime(date);
        }

        return parseTime(date.trim() + " " + hour.trim());
    }


}
