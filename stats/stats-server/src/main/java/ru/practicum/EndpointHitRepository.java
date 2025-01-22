package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value = "select app, uri, count(ip) as hits " +
            "from endpoint_hit " +
            "where timestamp > ?2 " +
            "  and timestamp < ?3 " +
            "  and (uri in (?1) or (coalesce(?1) = '0')) " +
            "group by app, uri order by hits desc ", nativeQuery = true)
    public List<ViewStats> getStats(List<String> valueUri, LocalDateTime start, LocalDateTime end);

    @Query(value = "select app, uri, count(distinct ip) as hits " +
            "from endpoint_hit " +
            "where timestamp > ?2 " +
            "  and timestamp < ?3 " +
            "  and (uri in (?1) or (coalesce(?1) = '0')) " +
            "group by app, uri order by hits desc", nativeQuery = true)
    public List<ViewStats> getStatsWithUniqueIp(List<String> valueUri, LocalDateTime start, LocalDateTime end);
}
