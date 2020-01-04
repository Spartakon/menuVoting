package ru.menuvoting.datetime;

import java.time.LocalDateTime;

import static ru.menuvoting.util.DateTimeUtil.*;

public class DateTimeBean {
    private int numTest = KEY_CURRENT_DATE_TIME;

    public DateTimeBean() {
    }

    public void setIsTest(int numTest) {
        this.numTest = numTest;
    }

    public LocalDateTime getDATE_TIME_CURRENT() {
        if (numTest == KEY_CURRENT_DATE_TIME) {
            return getCurrentDateTime();
        } else if (numTest == KEY_FOR_TEST_BEFORE) {
            return DATE_TIME_FOR_TEST_BEFORE;
        } else {
            return DATE_TIME_FOR_TEST_AFTER;
        }
    }
}
