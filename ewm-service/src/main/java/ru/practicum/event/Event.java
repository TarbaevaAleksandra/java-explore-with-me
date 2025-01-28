package ru.practicum.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.Location;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "annotation")
    private String annotation;
    @Column(name = "category")
    private Long category;
    @Column(name = "description")
    private String description;
    @Column(name = "eventDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @Embedded
    @Column(name = "location")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participantLimit")
    private Long participantLimit;
    @Column(name = "requestModeration")
    private Boolean requestModeration;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    @Enumerated(EnumType.STRING)
    private State state;
}
