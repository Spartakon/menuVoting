package ru.menuvoting.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.menuvoting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VoteRepository {

    @Autowired
    private CrudVoteRepository crudVoteRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Autowired
    private CrudRestaurantRepository crudRestaurantRepository;

    @Transactional
    public Vote save(Vote vote, int userId, int restaurantId, LocalDate date) {
        if (!vote.isNew() && get(vote.getId(), userId) == null) {
            return null;
        }
        vote.setDate(date);
        vote.setUser(crudUserRepository.getOne(userId));
        vote.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudVoteRepository.save(vote);
    }

    public Vote get(int id, int userId) {
        return crudVoteRepository.findById(id).filter(vote -> vote.getUser().getId() == userId).orElse(null);
    }

    public List<Vote> getAll(int userId) {
        return crudVoteRepository.getAll(userId);
    }

    public Vote getWithRestaurant(int id, int userId) {
        return crudVoteRepository.getWithRestaurant(id, userId);
    }

    public List<Vote> getAllWithRestaurant(int userId) {
        return crudVoteRepository.getAllWithRestaurant(userId);
    }
}
