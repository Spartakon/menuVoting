package ru.menuvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.menuvoting.model.Restaurant;
import ru.menuvoting.repository.RestaurantRepository;

import java.util.List;

import static ru.menuvoting.util.ValidationUtil.checkNotFound;
import static ru.menuvoting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(repository.getByName(name), "name=" + name);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    public List<Restaurant> getAllWithVotes() {
        return repository.getAllWithVotes();
    }
}
