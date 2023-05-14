package ru.nfm.voting.web.controller.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Vote;
import ru.nfm.voting.repository.VoteRepository;
import ru.nfm.voting.web.AbstractControllerTest;
import ru.nfm.voting.web.controller.restaurant.RestaurantTestData;
import ru.nfm.voting.web.controller.user.UserTestData;


import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL;
    public static final String RESTAURANT_PARAM = "restaurantId";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/for-today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.USER_VOTE_1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/for-today"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes/"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + VoteTestData.INVALID_VOTE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
                        .param(RESTAURANT_PARAM, RestaurantTestData.RESTAURANT_ID_1.toString()))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(voteRepository.getReferenceById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param(RESTAURANT_PARAM, RestaurantTestData.INVALID_RESTAURANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param(RESTAURANT_PARAM, RestaurantTestData.RESTAURANT_ID_1.toString()))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL)
                .param(RESTAURANT_PARAM, RestaurantTestData.INVALID_RESTAURANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateBeforeEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().plus(1, ChronoUnit.HOURS));
        perform(
                MockMvcRequestBuilders.patch(REST_URL)
                        .param(RESTAURANT_PARAM, RestaurantTestData.RESTAURANT_ID_2.toString()))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertEquals(RestaurantTestData.RESTAURANT_ID_2,
                voteRepository.getReferenceById(VoteTestData.USER_VOTE1_ID).getRestaurantId());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().minus(1, ChronoUnit.MINUTES));
        perform(MockMvcRequestBuilders.patch(REST_URL)
                .queryParam(RESTAURANT_PARAM, RestaurantTestData.RESTAURANT_ID_2.toString()))
                .andDo(print())
                .andExpect(status().isConflict());

        assertEquals(RestaurantTestData.RESTAURANT_ID_1,
                voteRepository.getReferenceById(VoteTestData.USER_VOTE1_ID).getRestaurantId());
    }
}
