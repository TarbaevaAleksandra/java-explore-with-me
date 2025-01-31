package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.event.EventService;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventsPrivateContoller {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEvents(@PathVariable Long userId,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.findEvents(userId,from,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId,
                                  @Valid @RequestBody NewEventDto newEvent) {
        return eventService.saveEvent(userId,newEvent);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findUserEvent(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        return eventService.findUserEvent(userId,eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEvent) {
        return eventService.updateEvent(userId,eventId,updateEvent);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsOfEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        return eventService.findRequestsOnEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsOfEvent(@PathVariable Long userId,
                                                                @PathVariable Long eventId,
                                                                @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return eventService.updateStatusOfRequest(userId,eventId,updateRequest);
    }
}
