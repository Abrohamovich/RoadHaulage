package ua.ithillel.roadhaulage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserService userService;
    private final UserRatingService userRatingService;

    @Override
    public void run(String... args) {
        String email = System.getenv("ADMIN_EMAIL");
        Optional<UserDto> userDtoOptional = userService.findByEmail(email);
        if (userDtoOptional.isEmpty()) {
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setEnabled(true);
            userDto.setPassword(System.getenv("ADMIN_PASSWORD"));
            userDto.setPhoneCode(System.getenv("ADMIN_PHONE_CODE"));
            userDto.setPhone(System.getenv("ADMIN_PHONE_NUMBER"));
            userDto.setRole(UserRole.ADMIN);
            userDto.setFirstName("Road");
            userDto.setLastName("Haulage");
            userDto = userService.save(userDto);

            UserRatingDto userRatingDto = new UserRatingDto();
            userRatingDto.setUser(userDto);
            userRatingDto.setAverage(0.0);
            userRatingDto.setCount(0);
            userRatingService.save(userRatingDto);
        }
    }
}
