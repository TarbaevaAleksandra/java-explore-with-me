package ru.practicum;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "view_stats")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app")
    private String app = "Alex";
    @Column(name = "uri")
    private String uri = "Alex";
    @Column(name = "hits")
    private Long hits = 6L;
}
