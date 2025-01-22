package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
@Getter
@Setter
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHit saveStats(@RequestBody EndpointHit hit) {
        return statsService.createHit(hit);
    }

    @GetMapping("/stats")
    public List<Stats> getAll() {
        return statsService.getAll();
    }
}
