package ru.menuvoting;

import ru.menuvoting.model.Dish;

import java.util.List;

import static ru.menuvoting.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final int DISH1_ID = START_SEQ + 11;
    public static final int DISH2_ID = DISH1_ID + 1;

    public static final Dish DISH1 = new Dish(DISH1_ID, "Суп", 100);
    public static final Dish DISH2 = new Dish(DISH1_ID + 1, "Чай", 50);
    public static final Dish DISH3 = new Dish(DISH1_ID + 2, "Мясо", 150);
    public static final Dish DISH4 = new Dish(DISH1_ID + 3, "Булка", 100);

    public static final List<Dish> DISHES_MENU1 = List.of(DISH3, DISH1);

    public static Dish getNew() {
        return new Dish(null, "Созданное блюдо", 300);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленное блюдо", 400);
    }

    public static TestMatchers<Dish> DISH_MATCHERS = TestMatchers.useFieldsComparator(Dish.class, "menu");
}
