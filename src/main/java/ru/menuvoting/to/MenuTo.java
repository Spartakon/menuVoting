package ru.menuvoting.to;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MenuTo extends BaseTo {

    private final String name;

    private final LocalDate date;

    private final List<DishTo> dishes;

    // id and name for restaurant
    @ConstructorProperties({"id", "name", "date", "dishes"})
    public MenuTo(Integer id, String name, LocalDate date, List<DishTo> dishes) {
        super(id);
        this.name = name;
        this.date = date;
        this.dishes = dishes;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<DishTo> getDishes() {
        return dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTo that = (MenuTo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(date, that.date) &&
                Objects.equals(dishes, that.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, dishes);
    }

    @Override
    public String toString() {
        return "MenuTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", dishes=" + dishes +
                '}';
    }
}
