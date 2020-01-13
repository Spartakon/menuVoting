package ru.menuvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
public class MenuRepository {

    @Autowired
    private CrudMenuRepository crudMenuRepository;

    @Autowired
    private CrudRestaurantRepository crudRestaurantRepository;

    @Transactional
    public Menu save(Menu menu, int restaurantId) {
        if (!menu.isNew() && get(menu.getId(), restaurantId) == null) {
            return null;
        }
        menu.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudMenuRepository.save(menu);
    }

    public boolean delete(int id, int restaurantId) {
        return crudMenuRepository.delete(id, restaurantId) != 0;
    }

    public Menu get(int id, int restaurantId) {
        return crudMenuRepository.findById(id).filter(meal -> meal.getRestaurant().getId() == restaurantId).orElse(null);

    }

    public List<Menu> getAll(int restaurantId) {
        return crudMenuRepository.getAll(restaurantId);
    }

    public Menu getWithDishes(int id, int restaurantId) {
        return crudMenuRepository.getWithDishes(id, restaurantId);
    }

    public Menu getByDate(int restaurantId, LocalDate date) {
        return crudMenuRepository.getByDate(restaurantId, date);
    }

    public List<Menu> getAllByDateWithRestaurantAndDishes(LocalDate date) {
        return crudMenuRepository.getAllByDateWithRestaurantAndDishes(date);
    }
}
