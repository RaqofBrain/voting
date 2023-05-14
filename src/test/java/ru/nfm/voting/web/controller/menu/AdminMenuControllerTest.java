package ru.nfm.voting.web.controller.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Menu;
import ru.nfm.voting.repository.MenuRepository;
import ru.nfm.voting.to.MenuTo;
import ru.nfm.voting.util.JsonUtil;
import ru.nfm.voting.web.AbstractControllerTest;
import ru.nfm.voting.web.controller.user.UserTestData;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.nfm.voting.web.controller.menu.MenuTestData.MENU_NEW_DATE;
import static ru.nfm.voting.web.controller.restaurant.RestaurantTestData.RESTAURANT_ID_1;
import static ru.nfm.voting.web.controller.restaurant.RestaurantTestData.RESTAURANT_ID_2;

public class AdminMenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuController.REST_URL;
    private static final String REST_URL_MENU_ID = REST_URL + "/{menuId}";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.MENU_1_TODAY));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.INVALID_MENU_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-date", RESTAURANT_ID_2)
                .queryParam("menuDate", MenuTestData.MENU_2_TODAY.getMenuDate().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.MENU_2_TODAY));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.RESTAURANT_1_MENUS));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/menus/by-date")
                .queryParam("menuDate", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.REST_MENUS_ON_DATE));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newMenuTo = MenuTestData.getNewMenuTo();
        newMenuTo.setMenuDate(LocalDate.parse(MENU_NEW_DATE));
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isCreated());

        Menu created = MenuTestData.MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        Menu newMenu = MenuTestData.getNew();
        newMenu.setId(newId);
        newMenu.setMenuDate(LocalDate.parse(MENU_NEW_DATE));
        MenuTestData.MENU_MATCHER.assertMatch(created, newMenu);
        MenuTestData.MENU_MATCHER.assertMatch(menuRepository.getReferenceById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_ID_1)
                .queryParam("menuDate", MENU_NEW_DATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of(3, 2, 1)));
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT_ID_1)
                .param("menuDate", MenuTestData.MENU_1_TODAY.getMenuDate().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuTo update = MenuTestData.getNewMenuTo();
        update.setDishIds(Set.of(2, 1));
        perform(MockMvcRequestBuilders.patch(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(update)))
                .andExpect(status().isNoContent());

        Menu expectedUpdated = MenuTestData.getUpdated();
        expectedUpdated.setId(MenuTestData.MENU1_ID);
        expectedUpdated.setMenuDate(LocalDate.now());
        MenuTestData.MENU_MATCHER.assertMatch(
                menuRepository.findByDateAndRestaurantIdWithDishListAndRestaurant(RESTAURANT_ID_1,
                        MenuTestData.MENU_1_TODAY.getMenuDate()).orElseThrow(), expectedUpdated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.patch(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.MENU1_ID))
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.findByIdAndRestaurantIdWithDishList(MenuTestData.MENU1_ID, RESTAURANT_ID_1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_MENU_ID, RESTAURANT_ID_1, MenuTestData.RESTAURANT_2_MENU_1))
                .andExpect(status().isConflict());
    }
}
