package ru.nfm.voting.web.controller.dish;

import ru.nfm.voting.model.Dish;
import ru.nfm.voting.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static MatcherFactory.Matcher<Dish> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurantId");

    public static final int DISH1_ID = 1;

    public static final Dish DISH_1 = new Dish(DISH1_ID, "Пицца Карбонара", 469, 1);
    public static final Dish DISH_2 = new Dish(DISH1_ID + 1, "Пицца Додо", 519, 1);
    public static final Dish DISH_3 = new Dish(DISH1_ID + 2, "2 Додстера", 319, 1);
    public static final Dish DISH_4 = new Dish(DISH1_ID + 3, "Чизбургер", 59, 2);
    public static final Dish DISH_5 = new Dish(DISH1_ID + 4, "Биг Спешиал Ростбиф", 289, 2);
    public static final Dish DISH_6 = new Dish(DISH1_ID + 5, "Биг хит + Чизбургер", 209, 2);
    public static final Dish DISH_7 = new Dish(DISH1_ID + 6, "Блин Илья Муромец", 402, 3);
    public static final Dish DISH_8 = new Dish(DISH1_ID + 7, "Борщ с рубленым мясом", 227, 3);

    public static final List<Dish> DISH_LIST = List.of(DISH_1, DISH_2, DISH_3);

    public static Dish getNew() {
        return new Dish(null, "new Dish", 150, 1);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Updated Dish", 300, 4);
    }
}
