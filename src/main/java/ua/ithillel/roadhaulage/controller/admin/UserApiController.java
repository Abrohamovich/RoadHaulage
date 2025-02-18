package ua.ithillel.roadhaulage.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/api/user")
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @GetMapping("/find-all")
    public ResponseEntity<List<UserDto>> findAll(@RequestParam int page,
                                                 @RequestParam int pageSize) {
        return ResponseEntity.ok(userService.findAllPageable(page, pageSize));
    }

    @GetMapping("/find-by")
    public ResponseEntity<UserDto> findBy(@RequestParam(required = false) Long id,
                                            @RequestParam(required = false) String email) {
        if (email != null && !email.trim().isEmpty()) {
            return ResponseEntity.of(userService.findByEmail(email));
        }
        if (id != null) {
            return ResponseEntity.of(userService.findById(id));
        }
        return ResponseEntity.badRequest().build();
    }
}