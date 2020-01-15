package ru.menuvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.model.Menu;
import ru.menuvoting.model.Vote;
import ru.menuvoting.repository.MenuRepository;
import ru.menuvoting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.menuvoting.util.DateTimeUtil.getCurrentDateTime;
import static ru.menuvoting.util.ValidationUtil.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final MenuRepository menuRepository;

    public VoteService(VoteRepository voteRepository, MenuRepository menuRepository) {
        this.voteRepository = voteRepository;
        this.menuRepository = menuRepository;
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }

    @Transactional
    public void update(int id, int userId, int restaurantId) {
        LocalDateTime currentDateTime = getCurrentDateTime();
        checkTimeToUpdateVote(currentDateTime.toLocalTime());

        Vote vote = get(id, userId);
        checkDateVoting(vote.getDate(), currentDateTime);
        checkNotFoundWithId(voteRepository.save(vote, userId, restaurantId, currentDateTime.toLocalDate()), vote.getId());
    }

    @Transactional
    public Vote create(int userId, int restaurantId) {
        LocalDateTime currentDateTime = getCurrentDateTime();
        Menu menu = menuRepository.getByDate(restaurantId, currentDateTime.toLocalDate());
        checkNotFound(menu, "Menu to date=" + currentDateTime.toLocalDate().toString());
        return voteRepository.save(new Vote(), userId, restaurantId, currentDateTime.toLocalDate());
    }

    public Vote getWithRestaurant(int id, int userId) {
        return checkNotFoundWithId(voteRepository.getWithRestaurant(id, userId), id);
    }

    public List<Vote> getAllWithRestaurant(int userId) {
        return voteRepository.getAllWithRestaurant(userId);
    }
}
