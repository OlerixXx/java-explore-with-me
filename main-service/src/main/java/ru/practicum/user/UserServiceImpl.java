package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.request.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(UserDto userDto) {
        log.info("Создан USER: {}", userDto);
        return userRepository.save(UserMapper.toUser(userDto));
    }

    @Transactional
    public User update(UserDto userDto, Long userId) {
        log.info("Обновлён USER: {}", userDto);
        User user = UserMapper.toUpdateUser(userDto, userRepository.findById(userId).orElseThrow(NoSuchElementException::new));
        return userRepository.save(user);
    }

    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
    }

    public List<User> getAll(List<Long> ids, Pageable page) {
        if (ids.isEmpty()) {
            return userRepository.findAll(page).toList();
        } else {
            return userRepository.findAllByIdIn(ids, page);
        }
    }

    @Transactional
    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }
}
