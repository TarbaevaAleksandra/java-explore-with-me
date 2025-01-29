package ru.practicum.event;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.dto.*;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UsersRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@AllArgsConstructor
public class EventSevice {
    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<EventShortDto> findEvents(Long userId, Integer from, Integer size) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventShortDto> events = eventRepository.findEventsByUserId(userId, pageable).getContent()
                .stream()
                .map(EventMapper::fromModelToShortDto)
                .toList();
        return events;
    }

    @Transactional
    public EventFullDto saveEvent(Long userId, NewEventDto newEvent) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Категория не найдена"));
        Event event = EventMapper.toModelFromNewDto(newEvent,category,user);
        return EventMapper.fromModelToFullDto(eventRepository.save(event),0L);
    }

    @Transactional(readOnly = true)
    public EventFullDto findUserEvent(Long userId, Long eventId) {
        //List<ViewStatsDto> views = statsClient.getAll(null,null,null,null)
        return EventMapper.fromModelToFullDto(eventRepository.findByIdAndInitiatorId(eventId,userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Событие не найдено")),
                0L);
    }

    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventUpdate) {
        Event oldEvent = eventRepository.findByIdAndInitiatorId(eventId,userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Событие не найдено"));
        if (oldEvent.getState().equals(State.PUBLISHED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        if (eventUpdate.getEventDate() != null){
            LocalDateTime eventDate = LocalDateTime.parse(eventUpdate.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Дата и время, на которые намечено событие, не может быть раньше, чем через два часа");
            } else {
                oldEvent.setEventDate(eventDate);
            }
        }
        if (eventUpdate.getAnnotation() != null) {
            oldEvent.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getCategory() != null) {
            oldEvent.setCategory(categoryRepository.findById(eventUpdate.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Категория не найдена")));
        }
        if (eventUpdate.getDescription() != null) {
            oldEvent.setDescription(eventUpdate.getDescription());
        }
        if (eventUpdate.getLocation() != null) {
            oldEvent.setLocation(eventUpdate.getLocation());
        }
        if (eventUpdate.getPaid() != null) {
            oldEvent.setPaid(eventUpdate.getPaid());
        }
        if (eventUpdate.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(eventUpdate.getParticipantLimit());
        }
        if (eventUpdate.getRequestModeration() != null) {
            oldEvent.setRequestModeration(eventUpdate.getRequestModeration());
        }
        if (eventUpdate.getTitle() != null) {
            oldEvent.setTitle(eventUpdate.getTitle());
        }
        if (eventUpdate.getStateAction() != null) {
            switch (eventUpdate.getStateAction()) {
                case "SEND_TO_REVIEW":
                    oldEvent.setState(State.PENDING);
                    break;
                case "CANCEL_REVIEW":
                    oldEvent.setState(State.CANCELED);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.CONFLICT,"Неизвестное состояние");
            }
        }
        return EventMapper.fromModelToFullDto(eventRepository.save(oldEvent), 0L);
    }

    public List<EventFullDto> findEventsWithFilter(List<Long> users,
                                                   List<String> statesStr,
                                                   List<Long> categories,
                                                   LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd,
                                                   Integer from,
                                                   Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Specification<Event> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null) {
                CriteriaBuilder.In<Long> usersClause = criteriaBuilder.in(root.get("initiator"));
                for (Long user : users) {
                    usersClause.value(user);
                }
                predicates.add(usersClause);
            }
            if (statesStr != null) {
                List<State> states = statesStr.stream().map((String x)->(State.valueOf(x))).toList();
                CriteriaBuilder.In<State> statesClause = criteriaBuilder.in(root.get("state"));
                for (State state : states) {
                    statesClause.value(state);
                }
                predicates.add(statesClause);
            }
            if (categories != null) {
                CriteriaBuilder.In<Long> categoriesClause = criteriaBuilder.in(root.get("category"));
                for (Long category : categories) {
                    categoriesClause.value(category);
                }
                predicates.add(categoriesClause);
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("eventDate"), rangeStart));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
        );
        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        return events.stream()
                .map((x) -> (EventMapper.fromModelToFullDto(x,0L)))
                .toList();
    }

    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventUpdate) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Событие не найдено"));
        if (eventUpdate.getEventDate() != null){
            LocalDateTime eventDate = LocalDateTime.parse(eventUpdate.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Дата и время, на которые намечено событие, не может быть раньше, чем через час");
            } else {
                oldEvent.setEventDate(eventDate);
            }
        }
        if (eventUpdate.getStateAction() != null) {
            if (!oldEvent.getState().equals(State.PENDING)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Событие находится не в состоянии ожидания публикации");
            }
            switch (eventUpdate.getStateAction()) {
                case "REJECT_EVENT":
                    oldEvent.setState(State.CANCELED);
                    break;
                case "PUBLISH_EVENT":
                    oldEvent.setState(State.PUBLISHED);
                    oldEvent.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Неизвестный параметр состояния события");
            }
        }

        if (eventUpdate.getAnnotation() != null) {
            oldEvent.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getCategory() != null) {
            oldEvent.setCategory(categoryRepository.findById(eventUpdate.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Категория не найдена")));
        }
        if (eventUpdate.getDescription() != null) {
            oldEvent.setDescription(eventUpdate.getDescription());
        }
        if (eventUpdate.getLocation() != null) {
            oldEvent.setLocation(eventUpdate.getLocation());
        }
        if (eventUpdate.getPaid() != null) {
            oldEvent.setPaid(eventUpdate.getPaid());
        }
        if (eventUpdate.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(eventUpdate.getParticipantLimit());
        }
        if (eventUpdate.getRequestModeration() != null) {
            oldEvent.setRequestModeration(eventUpdate.getRequestModeration());
        }
        if (eventUpdate.getTitle() != null) {
            oldEvent.setTitle(eventUpdate.getTitle());
        }
        return EventMapper.fromModelToFullDto(eventRepository.save(oldEvent),0L);
    }

}
