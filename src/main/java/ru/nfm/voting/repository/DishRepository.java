package ru.nfm.voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.error.DataConflictException;
import ru.nfm.voting.model.Dish;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.restaurantId = :restaurantId")
    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurantId = :restaurantId")
    List<Dish> findAllByRestaurantId(int restaurantId);

    @Query("SELECT d.id FROM Dish d WHERE d.id IN :dishIds AND d.restaurantId <> :restaurantId")
    List<Integer> findInvalidDishIds(List<Integer> dishIds, int restaurantId);

    default Dish checkBelong(int id, int restaurantId) {
        return findByIdAndRestaurantId(id, restaurantId).orElseThrow(
                () -> new DataConflictException(
                        "Dish with id=" + id + " doesn't belong to Restaurant with id=" + restaurantId));
    }
}
