package ru.practicum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Getter
@Setter
public class StatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> createHit(@RequestBody EndpointHitDto hit) {
        return statsClient.createHit(hit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) Boolean unique) {
        return statsClient.getAll(start, end, uris, unique);
    }
}