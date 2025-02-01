package ru.practicum.rating;

import jakarta.persistence.*;
import ru.practicum.event.model.Event;
import ru.practicum.users.model.User;

@Entity
@Table(name = "rating")
public class Rating {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_event")
    private Event event;
    @Column(name = "rating")
    private Integer rating;
}
