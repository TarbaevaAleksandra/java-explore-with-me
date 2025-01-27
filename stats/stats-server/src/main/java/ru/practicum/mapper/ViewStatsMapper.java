package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

@UtilityClass
public class ViewStatsMapper {

    public static ViewStatsDto fromModelToDto(ViewStats stats) {
        return new ViewStatsDto(
                stats.getApp(),
                stats.getUri(),
                stats.getHits()
        );
    }
}
