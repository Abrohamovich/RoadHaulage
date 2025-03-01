package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.repository.UserRepository;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceDefault implements UserService{
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final UserMapper userMapper;

    @Override
    public Optional<UserDto> findById(long id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        log.info("Finding user by email: {}", email);
       return userRepository.findByEmail(email)
               .map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByPhoneCodeAndPhone(String phoneCode, String phone) {
        log.info("Finding user by phone code: {} and phone: {}", phoneCode, phone);
        return userRepository.findByPhoneCodeAndPhone(phoneCode, phone)
                .map(userMapper::toDto);
    }

    @Override
    public List<UserDto> findAllPageable(int page, int pageSize) {
        log.info("Finding all users pageable");
        return userRepository.findAll(PageRequest.of(page, pageSize))
                .stream()
                .map(userMapper::toDto)
                .toList();
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
        log.info("Verify email successful, user enabled: {}", userDto.isEnabled());
        return 0;
    }

    @Override
    public short verifyPassword(String token) {
        return verifyToken(token);
    }

    @Override
    public AuthUserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthUserDto> authUserDto = userRepository.findByEmail(email)
                .map(userMapper::toAuthDto);
        if(authUserDto.isEmpty()) {
            throw new UsernameNotFoundException("Cant find user with username: " + email);
        }
        return authUserDto.get();
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
}