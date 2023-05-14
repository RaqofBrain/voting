package ru.nfm.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.error.DataConflictException;
import ru.nfm.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(value = "Menu.dishListWithRestaurant")
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.menuDate = :menuDate")
    Optional<Menu> findByDateAndRestaurantIdWithDishListAndRestaurant(int restaurantId, LocalDate menuDate);

    @EntityGraph(value = "Menu.dishListWithRestaurant")
    @Query("SELECT m FROM Menu m WHERE m.menuDate = :menuDate ORDER BY m.restaurant.id")
    List<Menu> findAllByDateWithDishListAndRestaurant(LocalDate menuDate);

    @EntityGraph(value = "Menu.dishList")
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId ORDER BY m.menuDate DESC")
    List<Menu> findAllForRestaurantWithDishList(int restaurantId);

    @EntityGraph(value = "Menu.dishList")
    @Query("SELECT m FROM Menu m WHERE m.id = :id AND m.restaurant.id = :restaurantId")
    Optional<Menu> findByIdAndRestaurantIdWithDishList(int id, int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.id = :id AND m.restaurant.id = :restaurantId")
    Optional<Menu> findByIdAndRestaurant(int id, int restaurantId);

    default Menu checkBelong(int id, int restaurantId) {
        return findByIdAndRestaurant(id, restaurantId)
                .orElseThrow(() -> new DataConflictException(
                        "Menu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}
