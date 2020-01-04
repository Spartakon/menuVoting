package ru.menuvoting;

import ru.menuvoting.model.Vote;

import java.time.LocalDate;

import static ru.menuvoting.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final int VOTE1_ID = START_SEQ + 16;
    public static final int VOTE2_ID = VOTE1_ID + 1;
    public static final int VOTE4_ID = VOTE1_ID + 3;

    public static Vote getVote1() {
        Vote vote = new Vote();
        vote.setId(VOTE1_ID);
        vote.setDate(LocalDate.now());
        return vote;
    }

    public static Vote getVote3() {
        Vote vote = new Vote();
        vote.setId(VOTE2_ID + 1);
        vote.setDate(LocalDate.now().minusDays(1));
        return vote;
    }

    public static Vote getNew() {
        Vote vote = new Vote();
        vote.setDate(LocalDate.now());
        return vote;
    }

    public static Vote getUpdated() {
        Vote vote = new Vote();
        vote.setId(VOTE1_ID);
        vote.setDate(LocalDate.now());
        return vote;
    }

    public static Vote getUpdatedPastDate() {
        Vote vote = new Vote();
        vote.setId(VOTE4_ID);
        vote.setDate(LocalDate.now().minusDays(1));
        return vote;
    }

    public static TestMatchers<Vote> VOTE_MATCHERS = TestMatchers.useFieldsComparator(Vote.class, "user", "restaurant");
}
