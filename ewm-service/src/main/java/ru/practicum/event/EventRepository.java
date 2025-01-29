package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "select * from events " +
            "where initiator = ?1 ", nativeQuery = true)
    Page<Event> findEventsByUserId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long id, Long initiatorId);

    Page<Event> findAll(@Nullable Specification<Event> spec, Pageable pageable);

    List<Event> findAllByIdIn(List<Long> ids);

}
