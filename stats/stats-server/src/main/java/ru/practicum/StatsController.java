package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
@Getter
@Setter
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHitDto saveStats(@RequestBody EndpointHitDto hit) {
        return statsService.createHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam(defaultValue = "0000-01-01 00:00:00")
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam(defaultValue = "4000-12-31 00:00:00")
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false,defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
