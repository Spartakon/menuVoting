package ru.menuvoting.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.MenuTestData;
import ru.menuvoting.model.Menu;
import ru.menuvoting.service.MenuService;
import ru.menuvoting.util.exception.ErrorType;
import ru.menuvoting.util.exception.NotFoundException;
import ru.menuvoting.web.AbstractControllerTest;
import ru.menuvoting.web.json.JsonUtil;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.menuvoting.MenuTestData.*;
import static ru.menuvoting.RestaurantTestData.RESTAURANT1_ID;
import static ru.menuvoting.TestUtil.*;
import static ru.menuvoting.UserTestData.USER;
import static ru.menuvoting.UserTestData.ADMIN;

class MenuAdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MenuAdminRestController.REST_URL + '/';

    @Autowired
    private MenuService menuService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> MENU_MATCHERS.assertMatch(readFromJsonMvcResult(result, Menu.class), MENU1));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MENU2_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by")
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .param("date", String.valueOf(MENU1.getDate()))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> MENU_MATCHERS.assertMatch(readFromJsonMvcResult(result, Menu.class), MENU1));
    }

    @Test
    void getUnauth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrows(NotFoundException.class, () -> menuService.get(MENU1_ID, RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MENU2_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Menu updated = MenuTestData.getUpdated();

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());

        MENU_MATCHERS.assertMatch(menuService.get(MENU1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void updateDateError() throws Exception {
        Menu updated = MenuTestData.getUpdated();
        updated.setDate(LocalDate.now().minusDays(2));

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.details").value("ru.menuvoting.util.exception.CreateMenuForDateException: Don't create/update a menu for the last date"));
    }


    @Test
    void createWithLocation() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu))
                .with(userHttpBasic(ADMIN)))
                .andDo(print());

        Menu created = readFromJson(action, Menu.class);
        Integer newId = created.getId();
        newMenu.setId(newId);
        MENU_MATCHERS.assertMatch(created, newMenu);
        MENU_MATCHERS.assertMatch(menuService.get(newId, RESTAURANT1_ID), newMenu);
    }

    @Test
    void createDateError() throws Exception {
        Menu newMenu = MenuTestData.getNew();
        newMenu.setDate(LocalDate.now().minusDays(2));
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.details").value("ru.menuvoting.util.exception.CreateMenuForDateException: Don't create/update a menu for the last date"));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHERS.contentJson(MENUS_RESTAURANT1));
    }

    @Test
    void createInvalid() throws Exception {
        Menu invalid = new Menu(null, null);
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    void updateInvalid() throws Exception {
        Menu invalid = new Menu(MENU1_ID, null);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Menu invalid = new Menu(MENU1_ID, MENU3.getDate());

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.details").value("Menu with this date already exists"));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Menu invalid = new Menu(null, MENU1.getDate());
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(RESTAURANT1_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value(ErrorType.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.details").value("Menu with this date already exists"));
    }
}
