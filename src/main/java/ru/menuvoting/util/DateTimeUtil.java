package ru.menuvoting.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final LocalTime TIME_FINISHING_UPDATE_VOTE = LocalTime.of(11, 00);
    public static final LocalDateTime DATE_TIME_FOR_TEST_BEFORE = LocalDateTime.now().withHour(10).withMinute(15);
    public static final LocalDateTime DATE_TIME_FOR_TEST_AFTER = LocalDateTime.now().withHour(11).withMinute(00);

    public static final int KEY_CURRENT_DATE_TIME = 0;
    public static final int KEY_FOR_TEST_BEFORE = 1;
    public static final int KEY_FOR_TEST_AFTER = 2;

    private DateTimeUtil() {
    }

    public static LocalDate parseLocalDate(String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
