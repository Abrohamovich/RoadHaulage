package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.mapper.VerificationTokenMapper;
import ua.ithillel.roadhaulage.repository.UserRepository;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceDefault implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto save(UserDto userDto) {
        List<String> errors = new ArrayList<>();

        User user = userMapper.toEntity(userDto);

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.add("A user with email " + user.getEmail() + " already exists");}
        if (userRepository.findByPhoneCodeAndPhone(user.getPhoneCode(), user.getPhone()).isPresent()) {
            errors.add("A user with phone " + user.getPhoneCode() + user.getPhone() + " already exists");}
        if (!isValidPassword(user.getPassword())){
            errors.add("Password must contain at least one digit and one uppercase");}
        if(!errors.isEmpty()) throw new UserCreateException(String.join(". ", errors));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

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

        return userMapper.toDto(updatedUser);
    }

    @Override
    public Optional<UserDto> findById(long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
       return userRepository.findByEmail(email)
               .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByPhoneCodeAndPhone(String phoneCode, String phone) {
        return userRepository.findByPhoneCodeAndPhone(phoneCode, phone)
                .map(userMapper::toDto);
    }

    @Override
    public short verifyEmail(String token) {
        short st = verifyToken(token);
        if (st != 0) {
            return st;
        }
        UserDto userDto = verificationTokenService.getToken(token).get().getUser();
        userDto.setEnabled(true);
        userRepository.save(userMapper.toEntity(userDto));
        verificationTokenService.delete(verificationTokenService.getToken(token).get());
        return 0;
    }

    @Override
    public short verifyPassword(String token, String password) {
        short st = verifyToken(token);
        if (st != 0) {
            return st;
        }
        UserDto userDto = verificationTokenService.getToken(token).get().getUser();
        userDto.setPassword(password);
        userRepository.save(userMapper.toEntity(userDto));
        verificationTokenService.delete(verificationTokenService.getToken(token).get());
        return 0;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDto> userDto = userRepository.findByEmail(email)
                .map(userMapper::toDto);
        if(userDto.isEmpty()) {
            throw new UsernameNotFoundException("Cant find user with username: " + email);
        }
        return userDto.get();
    }

    private short verifyToken(String token) {
        Optional<VerificationTokenDto> verificationToken = verificationTokenService.getToken(token);
        if (verificationToken.isEmpty() || !verificationToken.get().getToken().equals(token)) {
            return 1;
        }
        if (verificationToken.get().getUser() == null) {
            return 2;
        }
        if (verificationToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return 3;
        }
        return 0;
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile("\\d").matcher(password).find()
                && Pattern.compile("[A-Z]").matcher(password).find();
    }
}