package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query(value = "select * from events where initiator = ?1 ", nativeQuery = true)
    Page<Event> findEventsByUserId(Long userId, Pageable pageable);

    List<Event> findAllByIdIn(List<Long> ids);

    Optional<Event> findByIdAndInitiatorId(Long id, Long initiatorId);

    Optional<Event> findByIdAndState(Long eventId, State state);

}
