package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.repository.UserRepository;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private VerificationTokenService verificationTokenService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
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
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
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
}
