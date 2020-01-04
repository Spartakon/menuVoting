package ru.menuvoting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id AND m.restaurant.id=:restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Override
    @Transactional
    Menu save(Menu item);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Menu> getAll(@Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id=?1 AND m.restaurant.id=?2")
    Menu getWithDishes(int id, int restaurantId);

    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant JOIN FETCH m.dishes WHERE m.date=:date ORDER BY m.restaurant.name")
    List<Menu> getForVoting(@Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId AND m.date=:date")
    Menu getByDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);
}
