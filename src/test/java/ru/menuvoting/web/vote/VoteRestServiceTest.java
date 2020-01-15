package ru.menuvoting.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.RestaurantTestData;
import ru.menuvoting.VoteTestData;
import ru.menuvoting.model.Vote;
import ru.menuvoting.service.VoteService;
import ru.menuvoting.web.AbstractControllerTest;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.menuvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.menuvoting.RestaurantTestData.RESTAURANT2_ID;
import static ru.menuvoting.RestaurantTestData.RESTAURANT2;
import static ru.menuvoting.TestUtil.*;
import static ru.menuvoting.UserTestData.USER;
import static ru.menuvoting.UserTestData.USER2;
import static ru.menuvoting.UserTestData.USER_ID;
import static ru.menuvoting.VoteTestData.*;
import static ru.menuvoting.util.DateTimeUtil.*;
import static ru.menuvoting.util.exception.ErrorType.VALIDATION_ERROR;
import static ru.menuvoting.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_VOTE;

class VoteRestServiceTest extends AbstractControllerTest {
    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteService voteService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> VOTE_MATCHERS.assertMatch(readFromJsonMvcResult(result, Vote.class), getVote1()));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VOTE2_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getWithRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> VOTE_MATCHERS.assertMatch(readFromJsonMvcResult(result, Vote.class), getVote1()));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void update() throws Exception {
        if (LocalTime.now().isBefore(TIME_FINISHING_UPDATE_VOTE)) {
            Vote updated = VoteTestData.getUpdated();
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                    .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER)))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            Vote actual = voteService.getWithRestaurant(VOTE1_ID, USER_ID);
            VOTE_MATCHERS.assertMatch(actual, updated);
            RestaurantTestData.RESTAURANT_MATCHERS.assertMatch(actual.getRestaurant(), RESTAURANT2);
        } else {
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                    .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                    .andExpect(jsonPath("$.details").value(
                            "ru.menuvoting.util.exception.TimeVotingException:" +
                                    " Don't change your vote after " + TIME_FINISHING_UPDATE_VOTE.toString()));
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateAfterFinishVotingError() throws Exception {
        if (LocalTime.now().isAfter(TIME_FINISHING_UPDATE_VOTE)) {
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                    .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                    .andExpect(jsonPath("$.details").value(
                            "ru.menuvoting.util.exception.TimeVotingException:" +
                                    " Don't change your vote after " + TIME_FINISHING_UPDATE_VOTE.toString()));
        } else {
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                    .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER)))
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    void updateMenuForLastDateError() throws Exception {
        if (LocalTime.now().isBefore(TIME_FINISHING_UPDATE_VOTE)) {
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VOTE4_ID)
                    .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER2)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                    .andExpect(jsonPath("$.details").value(
                            "ru.menuvoting.util.exception.TimeVotingException: Don't change a vote for a previous date")
                    );
        } else {
            mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + VOTE1_ID)
                    .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                    .andExpect(jsonPath("$.details").value(
                            "ru.menuvoting.util.exception.TimeVotingException:" +
                                    " Don't change your vote after " + TIME_FINISHING_UPDATE_VOTE.toString()));
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER2)))
                .andDo(print());

        Vote created = readFromJson(action, Vote.class);
        Integer newId = created.getId();
        newVote.setId(newId);
        VOTE_MATCHERS.assertMatch(created, newVote);

        Vote actual = voteService.getWithRestaurant(newId, USER2.getId());
        VOTE_MATCHERS.assertMatch(voteService.get(newId, USER2.getId()), newVote);
        RestaurantTestData.RESTAURANT_MATCHERS.assertMatch(actual.getRestaurant(), RESTAURANT2);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createAgainToDayError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.details").value(EXCEPTION_DUPLICATE_VOTE));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHERS.contentJson(getVote1(), getVote3()));
    }
}
