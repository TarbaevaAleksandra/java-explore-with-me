package ru.practicum.event;

import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EventMapper {
    public static EventFullDto fromModelToFullDto(Event event, Map<Long, Long> views) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.fromModelToDto(event.getCategory()),
                0,
                LocalDateTime.now().toString(),
                event.getDescription(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(event.getEventDate()),
                event.getId(),
                UserMapper.fromModelToShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                null,
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                views.get(event.getId())
        );
    }

    public static EventShortDto fromModelToShortDto(Event event, Map<Long, Long> views) {
        return new EventShortDto(
                event.getAnnotation(),
                null,
                0L,
                event.getEventDate().toString(),
                event.getId(),
                null,
                event.getPaid(),
                event.getTitle(),
                views.get(event.getId())
        );
    }

    public static Event toModelFromNewDto(NewEventDto event, Category category, User user) {
        return new Event(
                event.getAnnotation(),
                category,
                event.getDescription(),
                LocalDateTime.parse(event.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                event.getLocation(),
                LocalDateTime.now(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getTitle(),
                user,
                State.PENDING
        );
    }
}
