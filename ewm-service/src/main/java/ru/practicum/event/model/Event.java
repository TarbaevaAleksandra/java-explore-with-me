package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ru.practicum.category.Category;
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
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "eventDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "created_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @Embedded
    @Column(name = "location")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participantLimit")
    private Integer participantLimit;
    @Column(name = "published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @Column(name = "requestModeration")
    private Boolean requestModeration;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    @Enumerated(EnumType.STRING)
    private State state;

    public Event(String annotation,
                 Category category,
                 String description,
                 LocalDateTime eventDate,
                 Integer confirmedRequests,
                 Location location,
                 LocalDateTime createdOn,
                 Boolean paid,
                 Integer participantLimit,
                 Boolean requestModeration,
                 String title,
                 User initiator,
                 State state) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.confirmedRequests = confirmedRequests;
        this.location = location;
        this.createdOn = createdOn;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
        this.initiator = initiator;
        this.state = state;
    }
}
