package ru.practicum.rating;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.rating.dto.NewRatingDto;
import ru.practicum.rating.dto.UserRatingDto;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.RequestRepository;
import ru.practicum.users.repository.UsersRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;
    private final RequestRepository requestRepository;

    //выставление рейтинга события
    @Transactional
    public Rating saveRating(NewRatingDto newRating, Long userId, Long eventId) {
        //проверка рейтинга
        Optional<Rating> oldRating = ratingRepository.findAllByEventIdAndUserId(eventId,userId);
        if (oldRating.isPresent())
            throw new DataConflictException("Рейтинг уже был выставлен");
        //проверка пользователя
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        //проверка события
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        if (event.getInitiator().getId().equals(userId))
            throw new DataConflictException("Инициатор события не может выставлять рейтинг своему событию");
        if (!event.getState().equals(State.PUBLISHED))
            throw new DataNotFoundException("Событие не опубликовано");
        //проверка подтвержденного участия в событии
        Optional<Request> request = requestRepository
                .findAllByEventIdAndUserIdAndStatus(eventId,userId,"CONFIRMED");
        if (request.isEmpty())
            throw new DataNotFoundException("Пользователь не участвовал в данном событии");
        //сохранение рейтинга
        Rating rating = RatingMapper.toModelFromNewDto(user, event, newRating);
        return ratingRepository.save(rating);
    }

    @Transactional
    public Rating updateRating(NewRatingDto newRating, Long userId, Long eventId) {
        //проверка рейтинга
        Rating oldRating = ratingRepository.findAllByEventIdAndUserId(eventId,userId)
                .orElseThrow(() -> (new DataConflictException("Рейтинг еще не был выставлен")));
        oldRating.setRating(newRating.getRating());
        return ratingRepository.save(oldRating);
    }

    @Transactional
    public void deleteRating(Long userId, Long eventId) {
        //проверка рейтинга
        Rating oldRating = ratingRepository.findAllByEventIdAndUserId(eventId,userId)
                .orElseThrow(() -> (new DataConflictException("Рейтинг еще не был выставлен")));
        ratingRepository.deleteById(oldRating.getId());
    }

    @Transactional(readOnly = true)
    public EventRatingDto findByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Событие не найдено"));
        Float avgRating = ratingRepository.getRatingOfEvent(eventId);
        if (avgRating == null)
            throw new DataNotFoundException("У события не был проставлен рейтинг");
        return RatingMapper.toEventRatingDto(event,avgRating);
    }

    @Transactional(readOnly = true)
    public UserRatingDto findByUserId(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));
        Float avgRating = ratingRepository.getRatingOfUser(userId);
        return RatingMapper.toUserEventDto(user,avgRating);
    }

    @Transactional(readOnly = true)
    public List<EventRatingDto> findEventsWithRating(String sort, Integer from, Integer size) {
        Sort sortBy = Sort.by("avg_rating").descending();
        int nullRating = 0; //параметр для правильной сортировки - события без рейтинга идут в конце
        if (sort.equals("asc")) {
            sortBy = Sort.by("avg_rating").ascending();
            nullRating = 10;
        }
        Pageable pageable = PageRequest.of(from / size, size, sortBy);
        List<ViewRating> viewRatings = ratingRepository.getEventsRating(nullRating, pageable);
        return viewRatings.stream().map(RatingMapper::toEventRatingDtoFromView).toList();
    }
}
