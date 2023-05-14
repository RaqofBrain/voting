package ru.nfm.voting.web.controller.restaurant;

import ru.nfm.voting.model.Restaurant;
import ru.nfm.voting.web.MatcherFactory;

import java.util.List;

import static ru.nfm.voting.web.controller.dish.DishTestData.DISH_LIST;

public class RestaurantTestData {
    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(
            Restaurant.class, "dishes");

    public static final Integer RESTAURANT_ID_1 = 1;
    public static final Integer RESTAURANT_ID_2 = 2;
    public static final Integer INVALID_RESTAURANT_ID = 9999;

    public static final Restaurant RESTAURANT_1 = new Restaurant(
            RESTAURANT_ID_1, "Додо пицца", "Большой пр. П.С. 33", "88005553535");
    public static final Restaurant RESTAURANT_2 = new Restaurant(
            RESTAURANT_ID_1 + 1, "Вкусно и точка", "Каменоостровский пр. 39", "88909890");
    public static final Restaurant RESTAURANT_3 = new Restaurant(
            RESTAURANT_ID_1 + 2, "Теремок", "Большой пр. П.С. 64", "9090808");


    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3);

    static {
        RESTAURANT_1.setDishes(DISH_LIST);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый ресторан", "Новый адрес 50", "+799898123");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_ID_1, "Обновленный ресторан", "Обновленный адрес 100", "+09090123");
    }
}
