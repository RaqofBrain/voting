package ru.nfm.voting.web.controller.menu;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nfm.voting.model.Menu;
import ru.nfm.voting.repository.MenuRepository;
import ru.nfm.voting.util.validation.ValidationUtil;

import java.net.URI;
import java.util.List;

import static ru.nfm.voting.web.controller.menu.AdminMenuController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuController {
    static final String REST_URL = "api/admin/menus";
    private final MenuRepository menuRepository;

    @GetMapping("/{id}")
    public Menu get(@PathVariable int id) {
        log.info("get menu with id : {}", id);
        return menuRepository.get(id);
    }

    @GetMapping
    public List<Menu> getAll() {
        log.info("get all menus");
        return menuRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Menu> createWithLocation(@RequestBody @Valid Menu menu) {
        log.info("create menu : {}", menu);
        ValidationUtil.checkNew(menu);
        Menu saved = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Menu menu, @PathVariable int id) {
        log.info("update menu : {} with id : {}", menu, id);
        ValidationUtil.assureIdConsistent(menu, id);
        menuRepository.save(menu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete menu with id : {}", id);
        menuRepository.delete(id);
    }

}
