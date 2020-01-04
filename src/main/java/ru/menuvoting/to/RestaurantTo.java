package ru.menuvoting.to;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class RestaurantTo extends BaseTo {

    private final String name;

    private final int votes;

    @ConstructorProperties({"id", "name", "votes"})
    public RestaurantTo(Integer id, String name, int votes) {
        super(id);
        this.name = name;
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo that = (RestaurantTo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(votes, that.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, votes);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                "name='" + name + '\'' +
                "votes=" + votes +
                '}';
    }
}
