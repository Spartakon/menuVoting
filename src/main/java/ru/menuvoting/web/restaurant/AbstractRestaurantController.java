package ru.menuvoting.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.menuvoting.model.Restaurant;
import ru.menuvoting.service.RestaurantService;
import ru.menuvoting.to.RestaurantTo;
import ru.menuvoting.util.RestaurantUtil;

import java.util.List;

import static ru.menuvoting.util.ValidationUtil.assureIdConsistent;
import static ru.menuvoting.util.ValidationUtil.checkNew;

public class AbstractRestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestaurantService service;

    public Restaurant get(int id) {
        log.info("get restaurant {}", id);
        return service.get(id);
    }

    public void delete(int id) {
        log.info("delete restaurant {}", id);
        service.delete(id);
    }

    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return service.getAll();
    }

    public Restaurant create(Restaurant restaurant) {
        checkNew(restaurant);
        log.info("create restaurant {}", restaurant);
        return service.create(restaurant);
    }

    public void update(Restaurant restaurant, int id) {
        assureIdConsistent(restaurant, id);
        log.info("update {} with id={}", restaurant, id);
        service.update(restaurant);
    }

    public Restaurant getByName(String name) {
        log.info("get restaurant by name {}", name);
        return service.getByName(name);
    }

    public List<RestaurantTo> getAllWithVotes() {
        log.info("getAll restaurants with votes");
        return RestaurantUtil.getTos(service.getAllWithVotes());
    }
}
