package ru.menuvoting;

import ru.menuvoting.model.Restaurant;
import ru.menuvoting.to.RestaurantTo;

import java.util.List;

import static ru.menuvoting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final int RESTAURANT1_ID = START_SEQ + 3;
    public static final int RESTAURANT2_ID = RESTAURANT1_ID + 1;
    public static final int RESTAURANT3_ID = RESTAURANT1_ID + 2;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "Ресторан 1");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT1_ID + 1, "Ресторан 2");
    public static final Restaurant RESTAURANT3 = new Restaurant(RESTAURANT1_ID + 2, "Ресторан 3");

    public static final List<Restaurant> RESTAURANTS = List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3);

    public static Restaurant getNew() {
        return new Restaurant(null, "Созданный ресторан");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Обновленный ресторан");
    }

    public static TestMatchers<Restaurant> RESTAURANT_MATCHERS = TestMatchers.useFieldsComparator(Restaurant.class, "votes");
    public static TestMatchers<RestaurantTo> RESTAURANT_TO_MATCHERS = TestMatchers.useEquals(RestaurantTo.class);
}
