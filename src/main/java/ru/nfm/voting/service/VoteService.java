package ru.nfm.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.error.DataConflictException;
import ru.nfm.voting.model.Vote;
import ru.nfm.voting.repository.VoteRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;


@Service
@AllArgsConstructor
public class VoteService {
    public static LocalTime VOTE_UPDATE_DEADLINE = LocalTime.of(11, 0, 0);
    private final VoteRepository voteRepository;

    @Transactional
    public Vote create(int userId, int restaurantId) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndVoteDate(userId, now.toLocalDate());
        if (voteOptional.isPresent()) {
            throw new DataConflictException("User already voted today");
        }
        Vote vote = new Vote(null, now, userId, restaurantId);
        return voteRepository.save(vote);
    }

    @Transactional
    public void update(int userId, int restaurantId) {
        LocalDateTime now = LocalDateTime.now();
        Vote vote = voteRepository.findByUserIdAndVoteDate(userId, now.toLocalDate())
                .orElseThrow(() -> new DataConflictException("User don't have vote for today"));

        if (now.toLocalTime().isAfter(VOTE_UPDATE_DEADLINE)) {
            throw new DataConflictException("Vote can't be updated after " + VOTE_UPDATE_DEADLINE);
        }
        vote.setVoteTime(now.toLocalTime());
        vote.setRestaurantId(restaurantId);
    }
}
