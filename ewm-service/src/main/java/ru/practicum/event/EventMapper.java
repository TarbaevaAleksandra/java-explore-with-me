package ru.practicum.event;

import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EventMapper {
    public static EventFullDto fromModelToFullDto(Event event, Map<Long, Long> views) {
        String publishedOn = "";
        if (event.getPublishedOn() != null)
            publishedOn = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(event.getEventDate());
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.fromModelToDto(event.getCategory()),
                event.getConfirmedRequests(),
                LocalDateTime.now().toString(),
                event.getDescription(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(event.getEventDate()),
                event.getId(),
                UserMapper.fromModelToShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                publishedOn,
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                views.get(event.getId())
        );
    }

    public static EventShortDto fromModelToShortDto(Event event, Map<Long, Long> views) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.fromModelToDto(event.getCategory()),
                event.getConfirmedRequests(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(event.getEventDate()),
                event.getId(),
                UserMapper.fromModelToShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views.get(event.getId())
        );
    }

    public static Event toModelFromNewDto(NewEventDto event, Category category, User user) {
        boolean paid = false;
        if (event.getPaid() != null)
            paid = event.getPaid();
        boolean reqModeration = true;
        if (event.getRequestModeration() != null)
            reqModeration = event.getRequestModeration();
        int limit = 0;
        if (event.getParticipantLimit() != null)
            limit = event.getParticipantLimit();
        return new Event(
                event.getAnnotation(),
                category,
                event.getDescription(),
                LocalDateTime.parse(event.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                0,
                event.getLocation(),
                LocalDateTime.now(),
                paid,
                limit,
                reqModeration,
                event.getTitle(),
                user,
                State.PENDING
        );
    }
}
