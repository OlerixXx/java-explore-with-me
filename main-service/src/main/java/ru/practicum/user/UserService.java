package ru.practicum.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.dto.request.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {

    User create(UserDto userDto);

    User update(UserDto userDto, Long userId);

    User getUser(Long userId);

    List<User> getAll(Pageable page);

    void remove(Long userId);
}
