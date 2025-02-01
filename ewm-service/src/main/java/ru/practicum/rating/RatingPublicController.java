package ru.practicum.rating;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rating")
@AllArgsConstructor
public class RatingPublicController {
    private final RatingService ratingService;

    @GetMapping("/{ratingId}")
    public Rating findById() {
        return ratingService.findById();
    }
}
