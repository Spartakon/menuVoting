package ru.menuvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.menuvoting.model.Menu;
import ru.menuvoting.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.menuvoting.util.ValidationUtil.*;

@Service
public class MenuService {

    private final MenuRepository repository;

    public MenuService(MenuRepository repository) {
        this.repository = repository;
    }

    public Menu get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(repository.delete(id, restaurantId), id);
    }

    public List<Menu> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }

    public void update(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must not be null");
        checkCreateMenuForDate(menu.getDate());
        checkNotFoundWithId(repository.save(menu, restaurantId), menu.getId());
    }

    public Menu create(Menu menu, int restaurantId) {
        Assert.notNull(menu, "menu must not be null");
        checkCreateMenuForDate(menu.getDate());
        return repository.save(menu, restaurantId);
    }

    public List<Menu> getForVoting() {
        return repository.getForVoting(LocalDate.now());
    }

    public Menu getByDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "date must not be null");
        return checkNotFoundWithDate(repository.getByDate(restaurantId, date), date);
    }
}
