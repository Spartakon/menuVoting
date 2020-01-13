package ru.menuvoting.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.menuvoting.model.Menu;
import ru.menuvoting.service.MenuService;
import ru.menuvoting.util.MenusUtil;
import ru.menuvoting.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.menuvoting.MenuTestData.*;
import static ru.menuvoting.RestaurantTestData.RESTAURANT1;
import static ru.menuvoting.RestaurantTestData.RESTAURANT2;
import static ru.menuvoting.TestUtil.userHttpBasic;
import static ru.menuvoting.UserTestData.USER;

public class MenuProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MenuProfileRestController.REST_URL + '/';

    @Autowired
    private MenuService menuService;

    @Test
    void getForVoting() throws Exception {
        List<Menu> menu = menuService.getAllTodayWithRestaurantAndDishes();
        menu.get(0).setRestaurant(RESTAURANT1);
        menu.get(1).setRestaurant(RESTAURANT2);

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_TO_MATCHERS.contentJson(MenusUtil.getTos(List.of(menu.get(0), menu.get(1)))));
    }
}
