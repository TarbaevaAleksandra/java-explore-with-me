package ru.practicum.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Getter
@Setter
@AllArgsConstructor
public class UsersRequestsPrivateController {
    private final UserService userService;

    @GetMapping
    public List<ParticipationRequestDto> findRequests(@PathVariable Long userId) {
        return userService.getRequests(userId);
    }

    @PostMapping
    public ParticipationRequestDto saveRequest(@PathVariable Long userId,
                              @RequestParam Long eventId) {
        System.out.println("PostMApping");
        return userService.saveRequest(userId,eventId);
    }

    @PatchMapping("/{requestId}/cansel")
    public ParticipationRequestDto canselRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        return userService.canselRequest(userId, requestId);
    }
}
