package ru.nfm.voting.web.controller.menu;

import ru.nfm.voting.model.Menu;
import ru.nfm.voting.to.MenuTo;
import ru.nfm.voting.web.MatcherFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static ru.nfm.voting.web.controller.dish.DishTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory
            .usingIgnoringFieldsComparator(Menu.class,
                    "restaurant", "dishList.id", "dishList.restaurantId", "dishList.menus");

    public static final int MENU1_ID = 1;
    public static final int RESTAURANT_2_MENU_1 = 4;
    public static final int INVALID_MENU_ID = 9999;
    public static final String MENU_NEW_DATE = LocalDate.now().plusDays(1).toString();

    public static final Menu MENU_1_TODAY = new Menu(MENU1_ID, LocalDate.now(), null);
    public static final Menu MENU_1_MINUS_1_DAY = new Menu(MENU1_ID + 1, LocalDate.now().minusDays(1), null);
    public static final Menu MENU_1_MINUS_2_DAY = new Menu(MENU1_ID + 2, LocalDate.now().minusDays(2), null);
    public static final Menu MENU_2_TODAY = new Menu(MENU1_ID + 3, LocalDate.now(), null);
    public static final Menu MENU_3_TODAY = new Menu(MENU1_ID + 4, LocalDate.now(), null);

    public static final List<Menu> RESTAURANT_1_MENUS = List.of(MENU_1_TODAY, MENU_1_MINUS_1_DAY, MENU_1_MINUS_2_DAY);
    public static final List<Menu> REST_MENUS_ON_DATE = List.of(MENU_1_TODAY, MENU_2_TODAY, MENU_3_TODAY);

    static {
        MENU_1_TODAY.setDishList(List.of(DISH_1));
        MENU_1_MINUS_1_DAY.setDishList(List.of(DISH_2));
        MENU_1_MINUS_2_DAY.setDishList(List.of(DISH_3));
        MENU_2_TODAY.setDishList(List.of(DISH_4, DISH_5, DISH_6));
        MENU_3_TODAY.setDishList(List.of(DISH_7, DISH_8));
    }

    public static Menu getNew() {
        return new Menu(null, null, null, List.of(DISH_1, DISH_2, DISH_3));
    }

    public static MenuTo getNewMenuTo() {
        return new MenuTo(null, new HashSet<>(List.of(2, 3, 1)), LocalDate.now());
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, MENU_1_TODAY.getMenuDate(), null, List.of(DISH_1, DISH_2));
    }
}
