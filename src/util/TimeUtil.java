package util;

import exception.InputException;
import main.Config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtil {

    public static String allMatter = "yyyy-MM-dd HH:mm";
    public static String dateMatter = "yyyy-MM-dd";
    public static String hourMatter = "HH:mm";

    public static DateTimeFormatter all_formatter = DateTimeFormatter.ofPattern(allMatter);
    public static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern(dateMatter);
    public static DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern(hourMatter);

    public static LocalDateTime parseTime(String time) throws InputException {
        try { // 1. 匹配allMatter
            return LocalDateTime.parse(time, all_formatter);
        } catch (DateTimeParseException ignored) {}

        try { // 2. 仅有日期 自动补00:00
            LocalDate date = LocalDate.parse(time, date_formatter);
            return date.atStartOfDay();
        } catch (DateTimeParseException ignored) {}


        try { // 3. 仅有时间, 自动补今天的日期
            LocalTime localTime = LocalTime.parse(time, hour_formatter);
            return LocalDateTime.of(LocalDate.now(), localTime);
        } catch (DateTimeParseException ignored) {}

        // 所有格式都不匹配
        throw new InputException(time + " 时间格式错误, 标准格式为2026-05-01 10:33");
    }

    public static LocalDateTime parseTime(String date, String hour) throws InputException {
        boolean dateEmpty = date == null || date.isEmpty();
        boolean hourEmpty = hour == null || hour.isEmpty();

        if(dateEmpty && hourEmpty) {
            return Config.invalidTime;
        }

        if(dateEmpty) {
            return parseTime(hour);
        }

        if(hourEmpty) {
            return parseTime(date);
        }

        return parseTime(date.trim() + " " + hour.trim());
    }

}
