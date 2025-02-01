package ru.practicum.rating;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    @Transactional
    public Rating saveRating(RatingDto newRating) {
        Rating rating = new Rating();
        return ratingRepository.save(rating);
    }

    @Transactional(readOnly = true)
    public Rating findById() {
        return new Rating();
    }
}
