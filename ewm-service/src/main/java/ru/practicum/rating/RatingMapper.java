package ru.practicum.rating;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Event;
import ru.practicum.users.model.User;

@UtilityClass
public class RatingMapper {

    public static Rating toModelFromNewDto(User user, Event event, NewRatingDto newRating) {
        return Rating.builder()
                .user(user)
                .event(event)
                .rating(newRating.getRating())
                .build();
    }
}