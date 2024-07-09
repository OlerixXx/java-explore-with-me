package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.request.UserDto;
import ru.practicum.user.dto.response.UserShortDto;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUser(UserDto userDto) {
        return new User(
                null,
                userDto.getName(),
                userDto.getEmail(),
                LocalDateTime.now()
        );
    }

    public static User toUpdateUser(UserDto userDto, User user) {
        return new User(
                user.getId(),
                userDto.getName() != null ? userDto.getName() : user.getName(),
                userDto.getEmail() != null ? userDto.getEmail() : user.getEmail(),
                user.getRegistrationDate()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
