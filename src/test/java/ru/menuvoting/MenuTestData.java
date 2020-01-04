package ru.menuvoting;

import ru.menuvoting.model.Menu;
import ru.menuvoting.to.MenuTo;

import java.time.LocalDate;
import java.util.List;

import static ru.menuvoting.model.AbstractBaseEntity.START_SEQ;

public class MenuTestData {
    public static final int MENU1_ID = START_SEQ + 6;
    public static final int MENU2_ID = MENU1_ID + 1;

    public static final Menu MENU1 = new Menu(MENU1_ID, LocalDate.now());
    public static final Menu MENU2 = new Menu(MENU1_ID + 1, LocalDate.now());
    public static final Menu MENU3 = new Menu(MENU1_ID + 2, LocalDate.now().plusDays(1));
    public static final Menu MENU4 = new Menu(MENU1_ID + 3, LocalDate.now().minusDays(1));

    public static final List<Menu> MENUS_RESTAURANT1 = List.of(MENU3, MENU1, MENU4);

    public static Menu getNew() {
        return new Menu(null, LocalDate.now().plusDays(1));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, LocalDate.now().plusDays(2));
    }

    public static TestMatchers<Menu> MENU_MATCHERS = TestMatchers.useFieldsComparator(Menu.class, "restaurant", "dishes");
    public static TestMatchers<MenuTo> MENU_TO_MATCHERS = TestMatchers.useEquals(MenuTo.class);
}
