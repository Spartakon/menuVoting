package ru.menuvoting.util;

import ru.menuvoting.HasId;
import ru.menuvoting.util.exception.CreateMenuForDateException;
import ru.menuvoting.util.exception.IllegalRequestDataException;
import ru.menuvoting.util.exception.NotFoundException;
import ru.menuvoting.util.exception.TimeVotingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.menuvoting.util.DateTimeUtil.TIME_FINISHING_VOTING;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFoundWithDate(T object, LocalDate date) {
        return checkNotFound(object, "date=" + date.toString());
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void checkCreateMenuForDate(LocalDate date) {
        if (date != null) {
            if (date.isBefore(DateTimeUtil.getCurrentDateTime().toLocalDate())) {
                throw new CreateMenuForDateException("Don't create/update a menu for the last date");
            }
        }
    }

    public static void checkDateVoting(LocalDate checkedDate, LocalDateTime current) {
        if (!checkedDate.equals(current.toLocalDate())) {
            throw new TimeVotingException("Don't change a vote for a previous date");
        }
    }

    public static void checkFinishingVoting(LocalTime time) {
        if (time.isAfter(TIME_FINISHING_VOTING)) {
            throw new TimeVotingException("Voting has already finished");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
