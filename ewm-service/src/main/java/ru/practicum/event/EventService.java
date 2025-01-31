package ru.practicum.event;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.exception.DataBadRequestException;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.users.mapper.RequestMapper;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.RequestRepository;
import ru.practicum.users.repository.UsersRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static ru.practicum.ConstantDateTime.FORMATTER;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final CategoryRepository categoryRepository;
    private final EventViewsComponent eventViewsComponent;
    private final RequestRepository requestRepository;

    // Private: События
    @Transactional(readOnly = true)
    public List<EventShortDto> findEvents(Long userId, Integer from, Integer size) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventShortDto> events = eventRepository.findEventsByUserId(userId, pageable).getContent()
                .stream()
                .map((x) -> (EventMapper.fromModelToShortDto(x,Map.of(0L,0L))))
                .toList();
        return events;
    }

    @Transactional
    public EventFullDto saveEvent(Long userId, NewEventDto newEvent) {
        LocalDateTime eventDate = LocalDateTime.parse(newEvent.getEventDate(),FORMATTER);
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataBadRequestException("Дата и время, на которые намечено событие, не может быть раньше, чем через два часа");
        }
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        Category category = categoryRepository.findById(newEvent.getCategory())
                .orElseThrow(() -> new DataNotFoundException("Категория не найдена"));
        Event event = eventRepository.save(EventMapper.toModelFromNewDto(newEvent,category,user));
        return EventMapper.fromModelToFullDto(event,Map.of(event.getId(),0L));
    }

    @Transactional(readOnly = true)
    public EventFullDto findUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId,userId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        Map<Long, Long> views = eventViewsComponent.getViewsOfEvents(List.of(event.getId()));
        return EventMapper.fromModelToFullDto(event,views);
    }

    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventUpdate) {
        Event oldEvent = eventRepository.findByIdAndInitiatorId(eventId,userId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        if (oldEvent.getState().equals(State.PUBLISHED)) {
            throw new DataConflictException("Изменить можно только отмененные события или события в состоянии ожидания модерации");
        }
        if (eventUpdate.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventUpdate.getEventDate(),FORMATTER);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DataBadRequestException("Дата и время, на которые намечено событие, не может быть раньше, чем через два часа");
            } else {
                oldEvent.setEventDate(eventDate);
            }
        }
        if (eventUpdate.getAnnotation() != null) {
            oldEvent.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getCategory() != null) {
            oldEvent.setCategory(categoryRepository.findById(eventUpdate.getCategory())
                    .orElseThrow(() -> new DataNotFoundException("Категория не найдена")));
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
                    throw new DataNotFoundException("Неизвестное состояние");
            }
        }
        Event event = eventRepository.save(oldEvent);
        Map<Long, Long> views = eventViewsComponent.getViewsOfEvents(List.of(event.getId()));
        return EventMapper.fromModelToFullDto(event, views);
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findRequestsOnEvent(Long userId, Long eventId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        List<Request> eventRequests = requestRepository.findAllByEventId(eventId);
        return eventRequests.stream().map(RequestMapper::fromModelToDto).toList();
    }

    @Transactional
    public EventRequestStatusUpdateResult updateStatusOfRequest(Long userId, Long eventId,
                                                                    EventRequestStatusUpdateRequest statusUpdate) {
        int requestsCount = statusUpdate.getRequestIds().size();
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        String status = statusUpdate.getStatus();
        List<Request> requests = requestRepository.findByIdIn(statusUpdate.getRequestIds());

        if (!Objects.equals(userId, event.getInitiator().getId()))
            throw new DataNotFoundException("Событие не доступно");
        for (Request request : requests) {
            if (!request.getStatus().equals("PENDING")) {
                throw new DataConflictException("Заявка не находится в состоянии ожидания");
            }
        }
        int confirmedRequests = 0;
        if (event.getConfirmedRequests() != null)
            confirmedRequests = event.getConfirmedRequests().intValue();

        switch (status) {
            case "CONFIRMED":
                if (event.getParticipantLimit() == 0 || !event.getRequestModeration()
                        || event.getParticipantLimit() > confirmedRequests + requestsCount) {
                    requests.forEach(request -> request.setStatus("CONFIRMED"));
                    event.setConfirmedRequests(confirmedRequests + requestsCount);
                    confirmed.addAll(requests);
                } else if (event.getParticipantLimit() <= confirmedRequests) {
                    throw new DataConflictException("Достигнут лимит заявок на участие в событии");
                } else {
                    for (Request request : requests) {
                        if (event.getParticipantLimit() > confirmedRequests) {
                            request.setStatus("CONFIRMED");
                            event.setConfirmedRequests(confirmedRequests + 1);
                            confirmed.add(request);
                        } else {
                            request.setStatus("REJECTED");
                            rejected.add(request);
                        }
                    }
                }
                break;
            case "REJECTED":
                requests.forEach(request -> request.setStatus("REJECTED"));
                rejected.addAll(requests);
        }
        eventRepository.save(event);
        requestRepository.saveAll(requests);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(
                confirmed.stream().map(RequestMapper::fromModelToDto).toList(),
                rejected.stream().map(RequestMapper::fromModelToDto).toList()
        );
        return result;
    }

    // Admin: События
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsWithFilter(List<Long> users,
                                                   List<String> states,
                                                   List<Long> categories,
                                                   LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd,
                                                   Integer from,
                                                   Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(Specification.where(null));
        if (users != null)
            specifications.add(inUsers(users));
        if (states != null)
            specifications.add(inStates(states));
        if (categories != null)
            specifications.add(inCategories(categories));
        if (rangeStart != null)
            specifications.add(eventDateGreaterThan(rangeStart));
        if (rangeEnd != null)
            specifications.add(eventDateLessThan(rangeEnd));
        Specification<Event> specification = specifications.stream().reduce(Specification::and).get();
        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        List<Long> idEvents = events.stream().map((x) -> (x.getId())).toList();
        Map<Long, Long> views = eventViewsComponent.getViewsOfEvents(idEvents);
        return events.stream()
                .map((x) -> (EventMapper.fromModelToFullDto(x,views)))
                .toList();
    }

    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest eventUpdate) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        if (eventUpdate.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(eventUpdate.getEventDate(),FORMATTER);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new DataBadRequestException("До события осталось менее одного часа");
            } else {
                oldEvent.setEventDate(eventDate);
            }
        }
        if (eventUpdate.getStateAction() != null) {
            if (!oldEvent.getState().equals(State.PENDING)) {
                throw new DataConflictException("Событие находится не в состоянии ожидания публикации");
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
                    throw new DataNotFoundException("Неизвестный параметр состояния события");
            }
        }

        if (eventUpdate.getAnnotation() != null) {
            oldEvent.setAnnotation(eventUpdate.getAnnotation());
        }
        if (eventUpdate.getCategory() != null) {
            oldEvent.setCategory(categoryRepository.findById(eventUpdate.getCategory())
                    .orElseThrow(() -> new DataNotFoundException("Категория не найдена")));
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
        Event event = eventRepository.save(oldEvent);
        Map<Long, Long> views = eventViewsComponent.getViewsOfEvents(List.of(event.getId())); //Map.of(0L,0L);
        return EventMapper.fromModelToFullDto(event,views);
    }

    // Public: События
    @Transactional(readOnly = true)
    public List<EventShortDto> findEventsByPublic(String text,
                                                  List<Long> categories,
                                                  Boolean paid,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable,
                                                  String sort,
                                                  Integer from,
                                                  Integer size,
                                                  HttpServletRequest request) {
        //сохранение статистики
        eventViewsComponent.saveStats("ewm-main-service",request.getRequestURI(),request.getRemoteAddr(),LocalDateTime.now());
        //условие сортировки
        Sort sortBy = Sort.by("id");
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    sortBy = Sort.by("eventDate");
                    break;
                case "VIEWS":
                    sortBy = Sort.by("views");
                    break;
            }
        }
        Pageable pageable = PageRequest.of(from / size, size, sortBy);
        //настройка условий поиска
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(Specification.where(null));
        if (text != null)
            specifications.add(annotationOrDescriptionLike(text));
        if (categories != null)
            specifications.add(inCategories(categories));
        if (paid != null)
            specifications.add(equalsPaid(paid));
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new DataBadRequestException("Данные начального и конечного времени заданы неправильно");
        }
        if (rangeStart != null)
            specifications.add(eventDateGreaterThan(rangeStart));
        else
            specifications.add(eventDateGreaterThan(LocalDateTime.now()));
        if (rangeEnd != null)
            specifications.add(eventDateLessThan(rangeEnd));
        if (onlyAvailable != null && onlyAvailable.equals(true))
            specifications.add(onlyAvailable());
        //получение списка событий
        Specification<Event> specification = specifications.stream().reduce(Specification::and).get();
        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        Map<Long, Long> views;
        if (!events.isEmpty()) {
            List<Long> idEvents = events.stream().map((x) -> (x.getId())).toList();
            views = eventViewsComponent.getViewsOfEvents(idEvents);
        } else {
            views = Map.of(0L,0L);
        }
        return events.stream()
                .map((x) -> (EventMapper.fromModelToShortDto(x,views)))
                .toList();
    }


    public EventFullDto findPublishedEvent(Long eventId, HttpServletRequest request) {
        eventViewsComponent.saveStats("ewm-main-service",request.getRequestURI(),request.getRemoteAddr(),LocalDateTime.now());
        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new DataNotFoundException("Опубликованного события не найдено"));
        Map<Long, Long> views = eventViewsComponent.getViewsOfEvents(List.of(event.getId()));
        return EventMapper.fromModelToFullDto(event, views);
    }

    private Specification<Event> inUsers(List<Long> users) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("initiator").get("id")).value(users);
            }
        };
    }

    private Specification<Event> inStates(List<String> states) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("state")).value(states);
            }
        };
    }

    private Specification<Event> inCategories(List<Long> categories) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.in(root.get("category").get("id")).value(categories);
            }
        };
    }

    private Specification<Event> eventDateGreaterThan(LocalDateTime rangeStart) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.greaterThan(root.get("eventDate"), rangeStart);
            }
        };
    }

    private Specification<Event> eventDateLessThan(LocalDateTime rangeEnd) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThan(root.get("eventDate"), rangeEnd);
            }
        };
    }

    private Specification<Event> annotationOrDescriptionLike(String text) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicateAnnotation = criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%");
                Predicate predicateDescription = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
                return criteriaBuilder.or(predicateAnnotation, predicateDescription);
            }
        };
    }

    private Specification<Event> equalsPaid(boolean paid) {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("paid"), paid);
            }
        };
    }

    private Specification<Event> onlyAvailable() {
        return new Specification<Event>() {
            @Override
            public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThan(root.get("confirmed_requests"), root.get("participant_limit"));
            }
        };
    }
}