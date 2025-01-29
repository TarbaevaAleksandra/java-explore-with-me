package ru.practicum.users.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.users.model.User;

@UtilityClass
public class UserMapper {
    public static User toModelFromDto(NewUserRequest newUserRequest) {
        return new User(
                newUserRequest.getEmail(),
                newUserRequest.getName()
        );
    }

    public static UserDto fromModelToDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    public static UserShortDto fromModelToShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
