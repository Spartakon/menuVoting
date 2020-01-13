package ru.menuvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.menuvoting.RestaurantTestData;
import ru.menuvoting.datetime.DateTimeBean;
import ru.menuvoting.model.Vote;
import ru.menuvoting.util.exception.NotFoundException;
import ru.menuvoting.util.exception.TimeVotingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.menuvoting.RestaurantTestData.RESTAURANT1;
import static ru.menuvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.menuvoting.RestaurantTestData.RESTAURANT2_ID;
import static ru.menuvoting.RestaurantTestData.RESTAURANT3_ID;
import static ru.menuvoting.UserTestData.USER2;
import static ru.menuvoting.UserTestData.USER_ID;
import static ru.menuvoting.VoteTestData.*;
import static ru.menuvoting.util.DateTimeUtil.KEY_FOR_TEST_AFTER;
import static ru.menuvoting.util.DateTimeUtil.KEY_FOR_TEST_BEFORE;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    protected VoteService service;

    @Autowired
    protected DateTimeBean dateTimeBean;

    @Test
    void create() throws Exception {
        Vote newVote = getNew();
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
        dateTimeBean.setIsTest(KEY_FOR_TEST_BEFORE);
        Vote updated = getUpdated();
        service.update(updated.getId(), USER_ID, RESTAURANT2_ID);
        VOTE_MATCHERS.assertMatch(service.get(VOTE1_ID, USER_ID), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        dateTimeBean.setIsTest(KEY_FOR_TEST_BEFORE);
        Vote updated = getUpdated();
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(updated.getId(), USER2.getId(), RESTAURANT1_ID));
        assertEquals(e.getMessage(), "Not found entity with id=" + VOTE1_ID);
    }

    @Test
    void updateAfterFinishVotingError() throws Exception {
        dateTimeBean.setIsTest(KEY_FOR_TEST_AFTER);
        Vote updated = getUpdated();
        assertThrows(TimeVotingException.class, () ->
                service.update(updated.getId(), USER_ID, RESTAURANT2_ID));
    }

    @Test
    void updateMenuForLastDateError() throws Exception {
        dateTimeBean.setIsTest(KEY_FOR_TEST_BEFORE);
        Vote updated = getUpdatedPastDate();
        assertThrows(TimeVotingException.class, () ->
                service.update(updated.getId(), USER2.getId(), RESTAURANT1_ID));
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
