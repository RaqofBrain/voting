package ru.nfm.voting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(name = "vote_date_user_id_unq_cs", columnNames = {"vote_date", "user_id"})
})
@NamedEntityGraph(name = "Vote.user",
        attributeNodes = {@NamedAttributeNode("user")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Vote extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "user_id", nullable = false)
    @Min(1)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "restaurant_id", nullable = false)
    @Min(1)
    private int restaurantId;

    @Column(name = "vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    @Column(name = "vote_time", nullable = false)
    @NotNull
    private LocalTime voteTime;

    public Vote(Integer id, LocalDateTime localDateTime, int userId, int restaurantId) {
        super(id);
        this.voteTime = localDateTime.toLocalTime();
        this.voteDate = localDateTime.toLocalDate();
        this.userId = userId;
        this.restaurantId = restaurantId;
    }
}
