package ru.practicum.rating;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.RequestRepository;
import ru.practicum.users.repository.UsersRepository;
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
        /*System.out.println(event.getEventDate() + " is After " + LocalDateTime.now());
        if (event.getEventDate().isAfter(LocalDateTime.now()))
            throw new DataNotFoundException("Событие еще не произошло");*/
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
    public Rating findById() {
        return null;
    }
}
