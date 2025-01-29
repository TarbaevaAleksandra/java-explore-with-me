package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Getter
@Setter
@AllArgsConstructor
public class EventsPrivateContoller {
    private final EventSevice eventSevice;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEvents(@PathVariable Long userId,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventSevice.findEvents(userId,from,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId,
                                  @RequestBody NewEventDto newEvent) {
        return eventSevice.saveEvent(userId,newEvent);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findUserEvent(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        return eventSevice.findUserEvent(userId,eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEvent) {
        return eventSevice.updateEvent(userId,eventId,updateEvent);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsOfEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        return List.of();
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsOfEvent(@PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return new EventRequestStatusUpdateResult();
    }
}
