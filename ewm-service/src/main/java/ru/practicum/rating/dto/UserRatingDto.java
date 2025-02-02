package ru.practicum.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserRatingDto {
    private Long id;
    private String name;
    private Float rating;
}
