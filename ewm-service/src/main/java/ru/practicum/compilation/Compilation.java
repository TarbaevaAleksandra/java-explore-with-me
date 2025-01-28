package ru.practicum.compilation;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.users.model.User;

@Entity
@Table(name = "categories")
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
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
}
