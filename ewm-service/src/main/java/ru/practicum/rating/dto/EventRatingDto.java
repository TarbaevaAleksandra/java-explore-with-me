package ru.practicum.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EventRatingDto {
    private String annotation;
    private String eventDate;
    private Long id;
    private String title;
    private String description;
    private Float rating;
}
