package ru.nfm.voting.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Dish;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
}
