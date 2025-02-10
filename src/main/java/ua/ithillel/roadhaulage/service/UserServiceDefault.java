package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.exception.UserCreateException;
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

    @Override
    public void save(User user) {
        List<String> errors = new ArrayList<>();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.add("A user with email " + user.getEmail() + " already exists");}
        if (userRepository.findByPhoneCodeAndPhone(user.getPhoneCode(), user.getPhone()).isPresent()) {
            errors.add("A user with phone " + user.getPhoneCode() + user.getPhone() + " already exists");}
        if (!isValidPassword(user.getPassword())){
            errors.add("Password must contain at least one digit and one uppercase");}
        if(!errors.isEmpty()) throw new UserCreateException(String.join(". ", errors));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        Optional<User> userFromDB = userRepository.findById(user.getId());
        if (userFromDB.isPresent() && !userFromDB.get().getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhoneCodeAndPhone(String phoneCode, String phone) {
        return userRepository.findByPhoneCodeAndPhone(phoneCode, phone);
    }

    @Override
    public short verifyEmail(String token) {
        short st = verifyToken(token);
        if (st != 0) {
            return st;
        }
        User user = verificationTokenService.getToken(token).get().getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenService.delete(verificationTokenService.getToken(token).get());
        return 0;
    }

    @Override
    public short verifyPassword(String token, String password) {
        short st = verifyToken(token);
        if (st != 0) {
            return st;
        }
        User user = verificationTokenService.getToken(token).get().getUser();
        user.setPassword(password);
        save(user);
        return 0;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Cant find user with username: " + email);
        }
        return user.get();
    }

    private short verifyToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenService.getToken(token);
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