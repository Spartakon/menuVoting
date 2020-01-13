package ru.menuvoting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.menuvoting.model.Menu;
import ru.menuvoting.util.exception.CreateMenuForDateException;
import ru.menuvoting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.menuvoting.MenuTestData.*;
import static ru.menuvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.menuvoting.RestaurantTestData.RESTAURANT2_ID;

public class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    protected MenuService service;

    @Test
    void delete() throws Exception {
        service.delete(MENU1_ID, RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(MENU1_ID, RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, RESTAURANT1_ID));
    }

    @Test
    void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(MENU1_ID, RESTAURANT2_ID));
    }

    @Test
    void create() throws Exception {
        Menu newMenu = getNew();
        Menu created = service.create(newMenu, RESTAURANT2_ID);
        Integer newId = created.getId();
        newMenu.setId(newId);
        MENU_MATCHERS.assertMatch(created, newMenu);
        MENU_MATCHERS.assertMatch(service.get(newId, RESTAURANT2_ID), created);
    }

    @Test
    void createDateError() throws Exception {
        Menu newMenu = getNew();
        newMenu.setDate(LocalDate.now().minusDays(2));
        assertThrows(CreateMenuForDateException.class, () -> service.create(newMenu, RESTAURANT1_ID));
    }

    @Test
    void get() throws Exception {
        Menu actual = service.get(MENU1_ID, RESTAURANT1_ID);
        MENU_MATCHERS.assertMatch(actual, MENU1);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1, RESTAURANT2_ID));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(MENU1_ID, RESTAURANT2_ID));
    }

    @Test
    void update() throws Exception {
        Menu updated = getUpdated();
        service.update(updated, RESTAURANT1_ID);
        MENU_MATCHERS.assertMatch(service.get(MENU1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(MENU1, RESTAURANT2_ID));
        assertEquals(e.getMessage(), "Not found entity with id=" + MENU1_ID);
    }

    @Test
    void updateDateError() throws Exception {
        Menu updated = getUpdated();
        updated.setDate(LocalDate.now().minusDays(2));
        assertThrows(CreateMenuForDateException.class, () -> service.update(updated, RESTAURANT1_ID));
    }

    @Test
    void getAll() throws Exception {
        MENU_MATCHERS.assertMatch(service.getAll(RESTAURANT1_ID), MENUS_RESTAURANT1);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(() -> service.create(new Menu(null, null), RESTAURANT1_ID), ConstraintViolationException.class);
    }

    @Test
    void getForVoting() throws Exception {
        MENU_MATCHERS.assertMatch(service.getAllTodayWithRestaurantAndDishes(), MENU1, MENU2);
    }

    @Test
    void getByDate() throws Exception {
        MENU_MATCHERS.assertMatch(service.getByDate(RESTAURANT1_ID, LocalDate.now()), MENU1);
    }
}
