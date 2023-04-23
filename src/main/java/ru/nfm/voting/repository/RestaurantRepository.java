package ru.nfm.voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
