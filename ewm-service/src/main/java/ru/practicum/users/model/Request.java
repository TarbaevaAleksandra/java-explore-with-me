package ru.practicum.users.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.event.Event;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_event")
    private Event event;
    private String status;

    public Request(LocalDateTime created, User user, Event event, String status) {
        this.created = created;
        this.user = user;
        this.event = event;
        this.status = status;
    }
}
