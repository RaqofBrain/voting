package ru.nfm.voting.web.controller.menu;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nfm.voting.model.Menu;
import ru.nfm.voting.repository.RestaurantRepository;
import ru.nfm.voting.service.MenuService;
import ru.nfm.voting.to.MenuTo;
import ru.nfm.voting.util.validation.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = {"menu", "menus"})
public class AdminMenuController extends AbstractMenuController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";
    private final MenuService service;
    private final RestaurantRepository restaurantRepository;

    @GetMapping(REST_URL + "/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restaurantId, @PathVariable int id) {
        restaurantRepository.getExisted(restaurantId);
        return ResponseEntity.of(menuRepository.findByIdAndRestaurantIdWithDishList(id, restaurantId));
    }

    @Override
    @GetMapping(REST_URL + "/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restaurantId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(restaurantId, menuDate == null ? LocalDate.now() : menuDate);
    }

    @Override
    @GetMapping("/api/admin/menus/by-date")
    public List<Menu> getAllByDate(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getAllByDate(menuDate == null ? LocalDate.now() : menuDate);
    }

    @GetMapping(REST_URL)
    public List<Menu> getAllForRestaurant(@PathVariable int restaurantId) {
        log.info("getAll menus for restaurant : {}", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        return menuRepository.findAllForRestaurantWithDishList(restaurantId);
    }

    @PostMapping(value = REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Menu> createWithLocation(@RequestBody @Valid MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create menu : {} for restaurant with id : {}", menuTo, restaurantId);
        ValidationUtil.checkNew(menuTo);
        Menu saved = service.create(menuTo, restaurantId, menuTo.getMenuDate());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(saved.getRestaurant().getId(), saved.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }

    @PatchMapping(value = REST_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@RequestBody @Valid MenuTo menuTo, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update menu with id : {} for restaurant : {}", id, restaurantId);
        ValidationUtil.assureIdConsistent(menuTo, id);
        service.update(menuTo, restaurantId, id);
    }

    @Transactional
    @DeleteMapping(REST_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete menu with id : {} for restaurant : {}", id, restaurantId);
        menuRepository.delete(menuRepository.checkBelong(id, restaurantId));
    }
}
