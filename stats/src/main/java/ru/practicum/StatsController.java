package ru.practicum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class StatsController {

    @PostMapping("/hit")
    public Stats saveStats() {
        Stats u = new Stats();
        return u;
    }

    @GetMapping("/stats")
    public Stats getStats() {
        Stats u = new Stats();
        return u;
    }
}

class Stats {
    public String app = "Alex";
    public String uri = "Alex";
    public String ip = "Alex";
    public String timestamp = "Alex";
}
