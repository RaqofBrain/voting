package ru.nfm.voting.web.controller.dish;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nfm.voting.model.Dish;
import ru.nfm.voting.repository.DishRepository;
import ru.nfm.voting.util.validation.ValidationUtil;

import java.net.URI;
import java.util.List;

import static ru.nfm.voting.web.controller.dish.AdminDishController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    static final String REST_URL = "/api/admin/dishes";
    private DishRepository dishRepository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get dish with id : {}", id);
        return dishRepository.get(id);
    }

    @GetMapping()
    public List<Dish> getAll() {
        log.info("get all dishes");
        return dishRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> createWithLocation(@RequestBody @Valid Dish dish) {
        log.info("create dish : {}", dish);
        ValidationUtil.checkNew(dish);
        Dish saved = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Dish dish, @PathVariable int id) {
        log.info("update dish : {} with restaurant id : {}", dish, id);
        ValidationUtil.assureIdConsistent(dish, id);
        dishRepository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish with id : {}", id);
        dishRepository.delete(id);
    }
}
