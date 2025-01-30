package ru.practicum.users.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class RequestMapper {
    public static User toModelFromDto(NewUserRequest newUserRequest) {
        return new User(
                newUserRequest.getEmail(),
                newUserRequest.getName()
        );
    }

    public static ParticipationRequestDto fromModelToDto(Request request) {
        return new ParticipationRequestDto(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(request.getCreated()),
                request.getEvent().getId(),
                request.getId(),
                request.getUser().getId(),
                request.getStatus()
        );
    }
}
