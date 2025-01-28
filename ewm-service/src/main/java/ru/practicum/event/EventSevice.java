package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Getter
@Setter
@AllArgsConstructor
public class EventSevice {
    private final EventRepository eventRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public List<EventShortDto> findEvents(Long userId, Integer from, Integer size) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        List<EventShortDto> events = eventRepository.findByInitiatorId(userId)
                .stream()
                .map(EventMapper::fromModelToShortDto)
                .toList();
        return events;
    }

    @Transactional
    public EventFullDto saveEvent(Long userId, NewEventDto newEvent) {
        //User newUser = UserMapper.toModelFromDto(newUserRequest);
        //return UserMapper.fromModelToDto(usersRepository.save(newUser));
        /*newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        newEvent.setState(State.PENDING);
        return eventRepository.save(newEvent);*/
        return new EventFullDto();
    }



}
