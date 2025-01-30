package ru.practicum.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class EventViewsComponent {
    private final StatsClient statsClient;
    private final Gson gson;
    private final ObjectMapper objectMapper;

    public void saveStats(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = new EndpointHitDto(app, uri, ip, timestamp);
        statsClient.createHit(endpointHitDto);
    }

    public Map<Long, Long> getViewsOfEvents(List<Long> events) {
        Map<Long, Long> viewsOfEvents = new HashMap<>();
        List<ViewStatsDto> viesStats;
        List<String> uris = new ArrayList<>();
        for (Long id : events) {
            uris.add("/events/" + id);
        }
        ResponseEntity<Object> response = statsClient.getAll("0000-01-01 00:00:00",
                "4000-01-01 00:00:00",
                uris,
                true);

        Object body = response.getBody();
        if (body != null) {
            String json = gson.toJson(body);
            TypeReference<List<ViewStatsDto>> typeRef = new TypeReference<>() {
            };
            try {
                viesStats = objectMapper.readValue(json, typeRef);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Ошибка сервиса статистики");
            }
            for (Long event : events) {
                viewsOfEvents.put(event, 0L);
            }
            if (!viesStats.isEmpty()) {
                for (ViewStatsDto stat : viesStats) {
                    if (!stat.getUri().equals("/events")) {
                        viewsOfEvents.put(Long.parseLong(stat.getUri().split("/", 0)[2]),
                                stat.getHits());
                    }
                }
            }
        }
        return viewsOfEvents;
    }
}