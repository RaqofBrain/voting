package ru.nfm.voting.web.controller.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import ru.nfm.voting.model.Restaurant;
import ru.nfm.voting.repository.RestaurantRepository;

import java.util.List;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Cacheable("restaurant")
    public Restaurant get(int id) {
        log.info("get restaurant with id : {}", id);
        return restaurantRepository.getExisted(id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        log.info("get all restaurants");
        return restaurantRepository.findAll();
    }
}
