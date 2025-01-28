package ru.practicum.event;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsPublicController {

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(required = false) Boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return List.of();
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable Long id) {
        return new EventFullDto();
    }
}
