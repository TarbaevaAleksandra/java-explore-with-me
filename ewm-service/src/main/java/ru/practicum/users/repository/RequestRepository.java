package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.users.model.Request;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findByIdIn(List<Long> requestIds);

    Optional<Request> findAllByEventIdAndUserId(Long eventId, Long userId);
}
