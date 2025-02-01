package ru.practicum.rating;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
@AllArgsConstructor
public class RatingPrivateController {
    private final RatingService ratingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating saveRating(@Valid @RequestBody RatingDto newRating) {
        return ratingService.saveRating(newRating);
    }
}
