package ru.practicum.rating;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    Optional<Rating> findAllByEventIdAndUserId(Long eventId, Long userId);

    @Query(value = "select avg(rating) as rating from ratings where id_event = ?1 ", nativeQuery = true)
    Float getRatingOfEvent(Long eventId);

    @Query(value = """
            select avg(r.rating) as rating 
              from ratings r 
              left join events e on e.id=r.id_event 
             where e.initiator = ?1 """, nativeQuery = true)
    Float getRatingOfUser(Long userId);

    @Query(value = """
            select * 
            from (select e.id, 
                         e.annotation, 
                         e.description, 
                         e.event_date, 
                         e.title, 
                         coalesce(avg(r.rating), ?1) as avg_rating
                    from events e 
               left join ratings r on r.id_event = e.id 
                   where e.state = 'PUBLISHED' 
                group by e.id
            ) q  """, nativeQuery = true)
    List<ViewRating> getEventsRating(int nullRating,Pageable pageable);

}
