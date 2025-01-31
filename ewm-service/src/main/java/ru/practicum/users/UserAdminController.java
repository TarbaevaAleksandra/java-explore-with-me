package ru.practicum.users;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findUsers(@RequestParam(required = false) List<Long> ids,
                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.findUsers(ids,from,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.saveUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
