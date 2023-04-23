package ru.nfm.voting.web.controller.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.nfm.voting.model.Dish;
import ru.nfm.voting.repository.DishRepository;
import ru.nfm.voting.web.AbstractControllerTest;
import ru.nfm.voting.web.controller.user.UserTestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminDishControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = AdminDishController.REST_URL + '/';

    @Autowired
    private DishRepository dishRepository;


    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + DishTestData.DISH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishTestData.dish1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + DishTestData.DISH_NOT_FOUND))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(AdminDishController.REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(
                        DishTestData.dish1, DishTestData.dish2, DishTestData.dish3, DishTestData.dish4));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(AdminDishController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(DishTestData.json(newDish)))
                .andExpect(status().isCreated());

        Dish created  = DishTestData.DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newDish);
        DishTestData.DISH_MATCHER.assertMatch(dishRepository.get(newId), newDish);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + DishTestData.DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(DishTestData.json(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(dishRepository.get(DishTestData.DISH1_ID), DishTestData.getUpdated());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + DishTestData.DISH1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findById(DishTestData.DISH1_ID).isPresent());
    }
}
