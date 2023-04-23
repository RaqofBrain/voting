package ru.nfm.voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nfm.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @Query("SELECT v FROM Vote v WHERE v.restaurant.id = :restaurantId AND v.voteDate = :voteDate ORDER BY v.voteTime")
    List<Vote> findAllForRestaurantByVoteDate(int restaurantId, LocalDate voteDate);

    @Query("SELECT v FROM Vote v WHERE v.voteDate = :voteDate ORDER BY v.voteDate, v.voteTime")
    List<Vote> findAllByVoteDate(LocalDate voteDate);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId and v.voteDate = :voteDate")
    Optional<Vote> findByUserIdAndVoteDate(int userId, LocalDate voteDate);
}
