package ru.menuvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.menuvoting.RestaurantTestData;
import ru.menuvoting.VoteTestData;
import ru.menuvoting.model.Vote;
import ru.menuvoting.repository.VoteRepository;
import ru.menuvoting.util.exception.NotFoundException;
import ru.menuvoting.util.exception.TimeVotingException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.menuvoting.RestaurantTestData.*;
import static ru.menuvoting.UserTestData.USER2;
import static ru.menuvoting.UserTestData.USER_ID;
import static ru.menuvoting.VoteTestData.*;
import static ru.menuvoting.util.DateTimeUtil.DATE_TIME_FOR_TEST_AFTER;
import static ru.menuvoting.util.DateTimeUtil.TIME_FINISHING_UPDATE_VOTE;
import static ru.menuvoting.util.ValidationUtil.*;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    protected VoteService service;

    @Autowired
    protected VoteRepository voteRepository;

    @Test
    void create() throws Exception {
        Vote newVote = VoteTestData.getNew();
        Vote created = service.create(USER2.getId(), RESTAURANT2_ID);
        Integer newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHERS.assertMatch(created, newVote);
        VOTE_MATCHERS.assertMatch(service.get(newId, USER2.getId()), newVote);
    }

    @Test
    void createAgainToDayError() throws Exception {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(USER_ID, RESTAURANT2_ID));
    }

    @Test
    void createWithNotMenuToDayError() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.create(USER2.getId(), RESTAURANT3_ID));
    }

    @Test
    void get() throws Exception {
        Vote actual = service.get(VOTE1_ID, USER_ID);
        VOTE_MATCHERS.assertMatch(actual, getVote1());
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(1, USER_ID));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(VOTE2_ID, USER_ID));
    }

    @Test
    void update() throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Vote updated = VoteTestData.getUpdated();

        if (currentDateTime.toLocalTime().isBefore(TIME_FINISHING_UPDATE_VOTE)) {
            service.update(updated.getId(), USER_ID, RESTAURANT2_ID);
        } else {
            voteRepository.save(updated, USER_ID, RESTAURANT2_ID, currentDateTime.toLocalDate());
        }
        Vote actual = service.getWithRestaurant(VOTE1_ID, USER_ID);
        VOTE_MATCHERS.assertMatch(actual, updated);
        RestaurantTestData.RESTAURANT_MATCHERS.assertMatch(actual.getRestaurant(), RESTAURANT2);
    }

    @Test
    void updateNotFound() throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Vote updated = VoteTestData.getUpdated();
        NotFoundException e;
        if (currentDateTime.toLocalTime().isBefore(TIME_FINISHING_UPDATE_VOTE)) {
            e = assertThrows(NotFoundException.class,
                    () -> service.update(updated.getId(), USER2.getId(), RESTAURANT1_ID));
        } else {
            e = assertThrows(NotFoundException.class,
                    () -> checkNotFoundWithId(voteRepository.save(
                            updated, USER2.getId(), RESTAURANT1_ID, currentDateTime.toLocalDate()), updated.getId()));
        }
        assertEquals(e.getMessage(), "Not found entity with id=" + VOTE1_ID);
    }

    @Test
    void updateAfterFinishVotingError() throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (currentDateTime.toLocalTime().isBefore(TIME_FINISHING_UPDATE_VOTE)) {
            assertThrows(TimeVotingException.class, () ->
                    checkTimeToUpdateVote(DATE_TIME_FOR_TEST_AFTER.toLocalTime()));
        } else {
            Vote updated = VoteTestData.getUpdated();
            assertThrows(TimeVotingException.class, () ->
                    service.update(updated.getId(), USER_ID, RESTAURANT2_ID));
        }
    }

    @Test
    void updateMenuForLastDateError() throws Exception {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Vote updated = getUpdatedPastDate();
        if (currentDateTime.toLocalTime().isBefore(TIME_FINISHING_UPDATE_VOTE)) {
            assertThrows(TimeVotingException.class, () ->
                    service.update(updated.getId(), USER2.getId(), RESTAURANT1_ID));
        } else {
            assertThrows(TimeVotingException.class, () ->
                    checkDateVoting(updated.getDate(), currentDateTime));
        }
    }

    @Test
    void getAll() throws Exception {
        VOTE_MATCHERS.assertMatch(service.getAll(USER_ID), getVote1(), getVote3());
    }

    @Test
    void getWithRestaurant() throws Exception {
        Vote actual = service.getWithRestaurant(VOTE1_ID, USER_ID);
        VOTE_MATCHERS.assertMatch(actual, getVote1());
        RestaurantTestData.RESTAURANT_MATCHERS.assertMatch(actual.getRestaurant(), RESTAURANT1);
    }

    @Test
    void getAllWithRestaurant() throws Exception {
        VOTE_MATCHERS.assertMatch(service.getAllWithRestaurant(USER_ID), getVote1(), getVote3());
    }
}
