package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.users.model.Request;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findByIdIn(List<Long> requestIds);
}
