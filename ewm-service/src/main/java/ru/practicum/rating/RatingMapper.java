package ru.practicum.rating;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Event;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.rating.dto.NewRatingDto;
import ru.practicum.rating.dto.UserRatingDto;
import ru.practicum.users.model.User;
import static ru.practicum.ConstantDateTime.FORMATTER;

@UtilityClass
public class RatingMapper {

    public static Rating toModelFromNewDto(User user, Event event, NewRatingDto newRating) {
        return Rating.builder()
                .user(user)
                .event(event)
                .rating(newRating.getRating())
                .build();
    }

    public static EventRatingDto toEventRatingDto(Event event, Float rating) {
        return EventRatingDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(FORMATTER.format(event.getEventDate()))
                .title(event.getTitle())
                .rating(rating)
                .build();
    }

    public static UserRatingDto toUserEventDto(User user, Float rating) {
        return UserRatingDto.builder()
                .id(user.getId())
                .name(user.getName())
                .rating(rating)
                .build();
    }

    public static EventRatingDto toEventRatingDtoFromView(ViewRating view) {
        float rating = view.getAvgRating();
        System.out.println(rating);
        if (rating == 10)
            rating = 0;
        return EventRatingDto.builder()
                .id(view.getId())
                .annotation(view.getAnnotation())
                .description(view.getDescription())
                .eventDate(FORMATTER.format(view.getEventDate()))
                .title(view.getTitle())
                .rating(rating)
                .build();
    }
}