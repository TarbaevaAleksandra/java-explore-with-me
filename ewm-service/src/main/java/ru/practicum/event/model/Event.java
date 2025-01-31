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
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Embedded
    @Column(name = "location")
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "title")
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    @Enumerated(EnumType.STRING)
    private State state;
}
