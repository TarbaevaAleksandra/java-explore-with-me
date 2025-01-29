package ru.practicum.compilation;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.event.Event;
import ru.practicum.users.model.User;

import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title")
    private String title;
    @ManyToMany
    @JoinTable(name = "compilations_of_events",
            joinColumns = @JoinColumn(name = "id_compilation"),
            inverseJoinColumns = @JoinColumn(name = "id_event"))
    private List<Event> events;

    public Compilation(Boolean pinned, String title, List<Event> events) {
        this.pinned = pinned;
        this.title = title;
        this.events = events;
    }
}
