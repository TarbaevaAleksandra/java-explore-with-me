package ru.practicum.rating.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.rating.dto.NewRatingDto;
import ru.practicum.rating.Rating;
import ru.practicum.rating.RatingService;

@RestController
@RequestMapping("/users/{userId}/rating")
@AllArgsConstructor
public class RatingPrivateController {
    private final RatingService ratingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating saveRating(@Valid @RequestBody NewRatingDto newRating,
                             @PathVariable Long userId,
                             @NotNull @RequestParam Long eventId) {
        return ratingService.saveRating(newRating,userId,eventId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Rating updateRating(@Valid @RequestBody NewRatingDto newRating,
                               @PathVariable Long userId,
                               @NotNull @RequestParam Long eventId) {
        return ratingService.updateRating(newRating,userId,eventId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable Long userId,
                             @NotNull @RequestParam Long eventId) {
        ratingService.deleteRating(userId,eventId);
    }
}
