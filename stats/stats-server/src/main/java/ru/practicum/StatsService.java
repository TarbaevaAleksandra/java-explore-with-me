package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@AllArgsConstructor
@Getter
@Setter
public class StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    public EndpointHit createHit(EndpointHit hit) {
        return endpointHitRepository.save(hit);
    }

    @Transactional(readOnly = true)
    public List<Stats> getAll() {
        return statsRepository.findAll();
    }


}