package ru.nfm.voting.web.controller.dish;

import ru.nfm.voting.model.Dish;
import ru.nfm.voting.util.JsonUtil;
import ru.nfm.voting.web.MatcherFactory;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "menus", "created");
    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH_NOT_FOUND = 10000;
    public static final Dish dish1 = new Dish(DISH1_ID, "Пицца Моцарелла", 3500);
    public static final Dish dish2 = new Dish(DISH2_ID, "Блин Илья Муромец", 1400);
    public static final Dish dish3 = new Dish(DISH3_ID, "Бургер с говядиной", 5000);
    public static final Dish dish4 = new Dish(DISH4_ID, "Пицца Прошуто Фунги", 4000);

    public static Dish getNew() {
        return new Dish(null, "new Dish", 3500);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Updated dish", 3000);
    }

    public static String json(Dish dish) {
        return JsonUtil.writeValue(dish);
    }
}
