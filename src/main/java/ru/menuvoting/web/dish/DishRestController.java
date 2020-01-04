package ru.menuvoting.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.menuvoting.View;
import ru.menuvoting.model.Dish;
import ru.menuvoting.service.DishService;

import java.net.URI;
import java.util.List;

import static ru.menuvoting.util.ValidationUtil.assureIdConsistent;
import static ru.menuvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    static final String REST_URL = "/rest/admin/dishes";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishService dishService;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id, @RequestParam int menuId) {
        log.info("get dish {} for menu {}", id, menuId);
        return dishService.get(id, menuId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @RequestParam int menuId) {
        log.info("delete dish {} for menu {}", id, menuId);
        dishService.delete(id, menuId);
    }

    @GetMapping
    public List<Dish> getAll(@RequestParam int menuId) {
        log.info("getAll for menu {}", menuId);
        return dishService.getAll(menuId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Rest.class) @RequestBody Dish dish, @PathVariable int id, @RequestParam int menuId) {
        assureIdConsistent(dish, id);
        log.info("update {} for menu {}", dish, menuId);
        dishService.update(dish, menuId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Validated(View.Rest.class) @RequestBody Dish dish, @RequestParam int menuId) {
        checkNew(dish);
        log.info("create {} for menu {}", dish, menuId);
        Dish created = dishService.create(dish, menuId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .queryParam("menuId", menuId)
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
