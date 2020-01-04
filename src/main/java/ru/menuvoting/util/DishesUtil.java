package ru.menuvoting.util;

import ru.menuvoting.model.Dish;
import ru.menuvoting.to.DishTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DishesUtil {

    private DishesUtil() {
    }

    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream().map(DishesUtil::createTo).collect(Collectors.toList());
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getName(), dish.getPrice());
    }
}
