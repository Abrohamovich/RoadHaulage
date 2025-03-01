package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.repository.UserRepository;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterServiceDefault implements RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto register(UserDto userDto) {
        List<String> errors = new ArrayList<>();

        User user = userMapper.toEntity(userDto);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.add("A user with email " + user.getEmail() + " already exists");}
        if (userRepository.findByPhoneCodeAndPhone(user.getPhoneCode(), user.getPhone()).isPresent()) {
            errors.add("A user with phone " + user.getPhoneCode() + user.getPhone() + " already exists");}
        if (!isValidPassword(user.getPassword())){
            errors.add("Password must contain at least one digit and one uppercase");}
        if(!errors.isEmpty()) {
            log.warn("Errors found: {}", String.join(". ", errors));
            throw new UserCreateException(String.join(". ", errors));
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        log.info("Saved user: {}", savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

        Optional<User> userFromDB = userRepository.findById(user.getId());
        if (userFromDB.isPresent() && !userFromDB.get().getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        User updatedUser = userRepository.save(user);

        log.info("Updated user: {}", updatedUser);
        return userMapper.toDto(updatedUser);
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile("\\d").matcher(password).find()
                && Pattern.compile("[A-Z]").matcher(password).find();
    }
}
