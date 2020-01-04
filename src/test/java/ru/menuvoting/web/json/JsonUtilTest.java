package ru.menuvoting.web.json;

import org.junit.jupiter.api.Test;
import ru.menuvoting.UserTestData;
import ru.menuvoting.model.Menu;
import ru.menuvoting.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.menuvoting.MenuTestData.*;

public class JsonUtilTest {

    @Test
    void readWriteValue() throws Exception {
        String json = JsonUtil.writeValue(MENU1);
        System.out.println(json);
        Menu menu = JsonUtil.readValue(json, Menu.class);
        MENU_MATCHERS.assertMatch(menu, MENU1);
    }

    @Test
    void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(MENUS_RESTAURANT1);
        System.out.println(json);
        List<Menu> menus = JsonUtil.readValues(json, Menu.class);
        MENU_MATCHERS.assertMatch(menus, MENUS_RESTAURANT1);
    }

    @Test
    void writeOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(UserTestData.USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = JsonUtil.writeAdditionProps(UserTestData.USER, "password", "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}