package ru.practicum.rating.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.rating.RatingService;
import ru.practicum.rating.dto.UserRatingDto;
import java.util.List;

@RestController
@RequestMapping("/rating")
@AllArgsConstructor
public class RatingPublicController {
    private final RatingService ratingService;

    @GetMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventRatingDto findByEventId(@PathVariable Long eventId) {
        return ratingService.findByEventId(eventId);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserRatingDto findByUserId(@PathVariable Long userId) {
        return ratingService.findByUserId(userId);
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventRatingDto> findEventsWithRating(@RequestParam(defaultValue = "desc") String sort,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        return ratingService.findEventsWithRating(sort,from,size);
    }
}
