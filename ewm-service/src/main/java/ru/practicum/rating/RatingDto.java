package ru.practicum.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RatingDto {
    @Min(1)
    @Max(5)
    private Integer rating;
}
