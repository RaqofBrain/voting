package ru.nfm.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface  VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.voteDate = :voteDate")
    Optional<Vote> findByUserIdAndVoteDate(int userId, LocalDate voteDate);

    @EntityGraph(value = "Vote.user")
    @Query("SELECT v FROM Vote v WHERE v.voteDate = :voteDate AND v.restaurantId = :restaurantId ORDER BY v.voteTime")
    List<Vote> findAllForRestaurantWithUser(int restaurantId, LocalDate voteDate);

    @EntityGraph(value = "Vote.user")
    @Query("SELECT v FROM Vote v WHERE v.voteDate = :voteDate ORDER BY v.restaurantId, v.voteTime")
    List<Vote> findAllByVoteDateWithUser(LocalDate voteDate);
}
