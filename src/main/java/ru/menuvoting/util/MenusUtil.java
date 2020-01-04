package ru.menuvoting.util;

import ru.menuvoting.model.Menu;
import ru.menuvoting.to.MenuTo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MenusUtil {

    private MenusUtil() {
    }

    public static List<MenuTo> getTos(Collection<Menu> menus) {
        return menus.stream().map(MenusUtil::createTo).collect(Collectors.toList());
    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.getRestaurant().getId(), menu.getRestaurant().getName(), menu.getDate(), DishesUtil.getTos(menu.getDishes()));
    }
}
