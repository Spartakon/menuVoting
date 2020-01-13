package ru.menuvoting.web.menu;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.menuvoting.to.MenuTo;

import java.util.List;

@RestController
@RequestMapping(value = MenuProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuProfileRestController extends AbstractMenuController {
    static final String REST_URL = "/rest/menus";

    @Override
    @GetMapping
    public List<MenuTo> getForVoting() {
        return super.getForVoting();
    }
}
