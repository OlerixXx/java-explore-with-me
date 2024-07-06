package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.convert.ConvertIntegerArray;
import ru.practicum.convert.ConvertPageable;
import ru.practicum.user.dto.request.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Validated(Create.class) UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@RequestBody @Validated(Update.class) UserDto userDto, @PathVariable Long userId) {
        return userService.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAll(@RequestParam(required = false, defaultValue = "") Integer[] ids,
                             @RequestParam(required = false, defaultValue = "0") Integer from,
                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getAll(ConvertIntegerArray.toLongList(ids), ConvertPageable.toMakePage(from, size));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long userId) {
        userService.remove(userId);
    }
}