package ru.nfm.voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.error.NotFoundException;
import ru.nfm.voting.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Override
    default Restaurant getExisted(int id) {
        return findById(id).orElseThrow(
                () -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }
}
