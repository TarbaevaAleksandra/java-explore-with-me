package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping
@RequiredArgsConstructor
@Getter
@Setter
public class StatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> createHit(@RequestBody EndpointHit hit) {
        return statsClient.createHit(hit);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        return statsClient.getAll();
    }
}