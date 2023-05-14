package ru.nfm.voting.web.controller.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nfm.voting.model.Vote;
import ru.nfm.voting.repository.RestaurantRepository;
import ru.nfm.voting.repository.VoteRepository;
import ru.nfm.voting.service.VoteService;
import ru.nfm.voting.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final VoteService service;

    @GetMapping("/for-today")
    public ResponseEntity<Vote> getByUserAndTodayDate(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        LocalDate today = LocalDate.now();
        log.info("get current vote on today : {} for user with id : {}", today, userId);
        return ResponseEntity.of(voteRepository.findByUserIdAndVoteDate(userId, today));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                   @RequestParam int restaurantId) {
        int userId = authUser.id();
        LocalDateTime now = LocalDateTime.now();
        log.info("create vote for user : {} for : {}", userId, now);
        restaurantRepository.getExisted(restaurantId);
        Vote created = service.create(userId, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/for-today")
                .buildAndExpand().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restaurantId) {
        int userId = authUser.id();
        LocalDateTime now = LocalDateTime.now();
        log.info("update vote for user : {} to : {}", userId, now);
        restaurantRepository.getExisted(restaurantId);
        service.update(userId, restaurantId);
    }
}
