package ru.nfm.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {
    @Query("SELECT m from Menu m WHERE m.menuDate >= :startDate AND m.menuDate <= :endDate")
    List<Menu> getBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restaurantId AND m.menuDate = :localDate")
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Menu> getMenuByRestaurantIdAndDate(int restaurantId, LocalDate localDate);
}
