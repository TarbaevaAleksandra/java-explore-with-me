package ru.practicum.users.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.users.model.Request;
import ru.practicum.users.model.User;
import static ru.practicum.ConstantDateTime.FORMATTER;

@UtilityClass
public class RequestMapper {
    public static User toModelFromDto(NewUserRequest newUserRequest) {
        return User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public static ParticipationRequestDto fromModelToDto(Request request) {
        return new ParticipationRequestDto(FORMATTER.format(request.getCreated()),
                request.getEvent().getId(),
                request.getId(),
                request.getUser().getId(),
                request.getStatus()
        );
    }
}
