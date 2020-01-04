package ru.menuvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.menuvoting.datetime.DateTimeBean;
import ru.menuvoting.model.Menu;
import ru.menuvoting.model.Vote;
import ru.menuvoting.repository.MenuRepository;
import ru.menuvoting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.menuvoting.util.ValidationUtil.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;
    private final DateTimeBean dateTimeBean;

    public VoteService(VoteRepository voteRepository, MenuRepository menuRepository, DateTimeBean dateTimeBean) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
        this.dateTimeBean = dateTimeBean;
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }

    @Transactional
    public void update(Vote vote, int userId, int restaurantId) {
        Assert.notNull(vote, "vote must not be null");

        LocalDateTime currentDateTime = dateTimeBean.getDATE_TIME_CURRENT();
        checkFinishingVoting(currentDateTime.toLocalTime());
        checkDateVoting(vote.getDate(), currentDateTime);
        checkNotFoundWithId(voteRepository.save(vote, userId, restaurantId, currentDateTime.toLocalDate()), vote.getId());
    }

    @Transactional
    public Vote create(Vote vote, int userId, int restaurantId) {
        Assert.notNull(vote, "vote must not be null");

        LocalDateTime currentDateTime = dateTimeBean.getDATE_TIME_CURRENT();
        checkFinishingVoting(currentDateTime.toLocalTime());

        Menu menu = menuRepository.getByDate(restaurantId, currentDateTime.toLocalDate());
        checkNotFound(menu, "Menu to date=" + currentDateTime.toLocalDate().toString());
        return voteRepository.save(vote, userId, restaurantId, currentDateTime.toLocalDate());
    }

    public Vote getWithRestaurant(int id, int userId) {
        return checkNotFoundWithId(voteRepository.getWithRestaurant(id, userId), id);
    }

    public List<Vote> getAllWithRestaurant(int userId) {
        return voteRepository.getAllWithRestaurant(userId);
    }
}
