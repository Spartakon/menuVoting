package ru.menuvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.menuvoting.model.Restaurant;
import ru.menuvoting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.menuvoting.RestaurantTestData.*;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    protected RestaurantService service;

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNew();
        Restaurant created = service.create(newRestaurant);
        Integer newId = created.getId();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHERS.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHERS.assertMatch(service.get(newId), created);
    }

    @Test
    void duplicateNameCreate() throws Exception {
        assertThrows(DataAccessException.class, () ->
                service.create(new Restaurant(null, RESTAURANT1.getName())));
    }

    @Test
    void delete() throws Exception {
        service.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () ->
                service.delete(RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() throws Exception {
        Restaurant actual = service.get(RESTAURANT1_ID);
        RESTAURANT_MATCHERS.assertMatch(actual, RESTAURANT1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByName() throws Exception {
        Restaurant restaurant = service.getByName(RESTAURANT1.getName());
        RESTAURANT_MATCHERS.assertMatch(restaurant, RESTAURANT1);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        service.update(updated);
        RESTAURANT_MATCHERS.assertMatch(service.get(RESTAURANT1_ID), updated);
    }

    @Test
    void getAll() throws Exception {
        RESTAURANT_MATCHERS.assertMatch(service.getAll(), RESTAURANTS);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Restaurant(null, "  ")), ConstraintViolationException.class);
    }

    @Test
    void getAllWithVotes() throws Exception {
        List<Restaurant> actual = service.getAllWithVotes();
        RESTAURANT_MATCHERS.assertMatch(actual, RESTAURANTS);
        assertEquals(2, actual.get(0).getVotes().size());
        assertEquals(RESTAURANT1.getName(), actual.get(0).getName());
        assertEquals(2, actual.get(1).getVotes().size());
        assertEquals(RESTAURANT2.getName(), actual.get(1).getName());
        assertEquals(0, actual.get(2).getVotes().size());
        assertEquals(RESTAURANT3.getName(), actual.get(2).getName());
    }
}
