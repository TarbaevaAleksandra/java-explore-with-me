package ru.practicum.compilation;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.event.model.Event;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compilation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pinned")
    private boolean pinned;
    @Column(name = "title")
    private String title;
    @ManyToMany
    @JoinTable(name = "compilations_of_events",
            joinColumns = @JoinColumn(name = "id_compilation"),
            inverseJoinColumns = @JoinColumn(name = "id_event"))
    private Set<Event> events;

    public Compilation(boolean pinned, String title, Set<Event> events) {
        this.pinned = pinned;
        this.title = title;
        this.events = events;
    }
}
