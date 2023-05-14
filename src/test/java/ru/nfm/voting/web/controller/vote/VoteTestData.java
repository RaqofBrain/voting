package ru.nfm.voting.web.controller.vote;

import ru.nfm.voting.model.Vote;
import ru.nfm.voting.service.VoteService;
import ru.nfm.voting.web.MatcherFactory;
import ru.nfm.voting.web.controller.restaurant.RestaurantTestData;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.nfm.voting.web.controller.user.UserTestData.GUEST_ID;
import static ru.nfm.voting.web.controller.user.UserTestData.USER_ID;

public class VoteTestData {
    public static MatcherFactory.Matcher<Vote> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant", "voteTime");

    public static final int USER_VOTE1_ID = 1;
    public static final int INVALID_VOTE_ID = 1000;

    public static final Vote USER_VOTE_1 = new Vote(USER_VOTE1_ID, LocalDateTime.now(),
            USER_ID, RestaurantTestData.RESTAURANT_ID_1);

    public static Vote getNew() {
        return new Vote(null, LocalDateTime.now(), GUEST_ID, 1);
    }

    public static void setVoteDeadLineTime(LocalTime voteTestTime) {
        VoteService.VOTE_UPDATE_DEADLINE = voteTestTime;
    }
}
