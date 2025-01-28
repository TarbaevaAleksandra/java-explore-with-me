package ru.practicum.event;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

public class EventMapper {
    public static EventFullDto fromModelToDto(Event newEvent) {
        return new EventFullDto();
    }

    public static EventShortDto fromModelToShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                null,
                null,
                event.getEventDate().toString(),
                event.getId(),
                null,
                event.getPaid(),
                event.getTitle(),
                null
        );
    }
}
