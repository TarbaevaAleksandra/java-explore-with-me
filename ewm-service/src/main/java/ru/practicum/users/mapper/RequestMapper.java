package ru.practicum.users.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;

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
                request.getCreated().toString(),
                request.getId(),
                request.getUser().getId(),
                request.getEvent().getId(),
                request.getStatus()
        );
    }
}
