package ru.menuvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.model.Dish;

import java.util.List;

@Repository
public class DishRepository {

    @Autowired
    private CrudDishRepository crudDishRepository;

    @Autowired
    private CrudMenuRepository crudMenuRepository;

    @Transactional
    public Dish save(Dish dish, int menuId) {
        if (!dish.isNew() && get(dish.getId(), menuId) == null) {
            return null;
        }
        dish.setMenu(crudMenuRepository.getOne(menuId));
        return crudDishRepository.save(dish);
    }

    public boolean delete(int id, int menuId) {
        return crudDishRepository.delete(id, menuId) != 0;
    }

    public Dish get(int id, int menuId) {
        return crudDishRepository.findById(id).filter(dish -> dish.getMenu().getId() == menuId).orElse(null);
    }

    public List<Dish> getAll(int menuId) {
        return crudDishRepository.getAll(menuId);
    }
}
