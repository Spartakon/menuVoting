package ru.menuvoting.web.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.menuvoting.model.Menu;
import ru.menuvoting.service.MenuService;
import ru.menuvoting.to.MenuTo;
import ru.menuvoting.util.MenusUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.menuvoting.util.ValidationUtil.assureIdConsistent;
import static ru.menuvoting.util.ValidationUtil.checkNew;

public abstract class AbstractMenuController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuService service;

    public Menu get(int id, int restaurantId) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    public void delete(int id, int restaurantId) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        service.delete(id, restaurantId);
    }

    public List<Menu> getAll(int restaurantId) {
        log.info("getAll for restaurant {}", restaurantId);
        return service.getAll(restaurantId);
    }

    public Menu create(Menu menu, int restaurantId) {
        checkNew(menu);
        log.info("create {} for restaurant {}", menu, restaurantId);
        return service.create(menu, restaurantId);
    }

    public void update(Menu menu, int id, int restaurantId) {
        assureIdConsistent(menu, id);
        log.info("update {} for restaurant {}", menu, restaurantId);
        service.update(menu, restaurantId);
    }

    public Menu getByDate(int restaurantId, LocalDate date) {
        log.info("get menu by date {} for restaurant {} ", date, restaurantId);
        return service.getByDate(restaurantId, date);
    }

    public List<MenuTo> getForVoting() {
        log.info("get all for voting");
        return MenusUtil.getTos(service.getForVoting());
    }
}
