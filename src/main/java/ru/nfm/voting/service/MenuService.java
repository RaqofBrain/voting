package ru.nfm.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.error.DataConflictException;
import ru.nfm.voting.model.Dish;
import ru.nfm.voting.model.Menu;
import ru.nfm.voting.model.Restaurant;
import ru.nfm.voting.repository.DishRepository;
import ru.nfm.voting.repository.MenuRepository;
import ru.nfm.voting.repository.RestaurantRepository;
import ru.nfm.voting.to.MenuTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Menu create(MenuTo menuTo, int restaurantId, LocalDate menuDate) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        List<Dish> dishes = getValidatedDishes(new ArrayList<>(menuTo.getDishIds()), restaurantId);
        Menu menu = new Menu(menuDate, restaurant, dishes);
        return menuRepository.save(menu);
    }

    @Transactional
    public void update(MenuTo menuTo, int restaurantId, int menuId) {
        Menu menu = menuRepository.checkBelong(menuId, restaurantId);
        List<Dish> dishes = getValidatedDishes(new ArrayList<>(menuTo.getDishIds()), restaurantId);
        menu.setDishList(dishes);
    }

    private List<Dish> getValidatedDishes(List<Integer> dishIds, int restaurantId) {
        List<Integer> invalidDishIds = dishRepository.findInvalidDishIds(dishIds, restaurantId);

        if (!invalidDishIds.isEmpty()) {
            throw new DataConflictException("Dishes with ids=" + invalidDishIds +
                    " don't belong to Restaurant with id=" + restaurantId);
        }

        return dishRepository.findAllById(dishIds);
    }
}
