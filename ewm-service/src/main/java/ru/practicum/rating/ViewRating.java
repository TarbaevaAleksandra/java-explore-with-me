package ru.practicum.rating;

import java.time.LocalDateTime;

public interface ViewRating {

    Long getId();

    String getAnnotation();

    String getDescription();

    Float getAvgRating();

    String getTitle();

    LocalDateTime getEventDate();
}
