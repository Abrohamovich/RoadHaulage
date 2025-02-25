package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.mapper.UserMapper;
import ua.ithillel.roadhaulage.mapper.VerificationTokenMapper;
import ua.ithillel.roadhaulage.repository.VerificationTokenRepository;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationTokenServiceDefault implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenMapper verificationTokenMapper;
    private final UserMapper userMapper;

    @Override
    public VerificationTokenDto save(VerificationTokenDto token) {
        VerificationToken saved = verificationTokenRepository
                .save(verificationTokenMapper.toEntity(token));
        log.info("Creating verification token: {}, id: {}, user id: {}, expire date: {}", saved.getToken(),
                saved.getId(), saved.getUser().getId(), saved.getExpiresAt());
        return verificationTokenMapper.toDto(saved);
    }

    @Override
    public Optional<VerificationTokenDto> getToken(String token) {
        log.info("Retrieving verification token by token: {}", token);
        return verificationTokenRepository.findByToken(token)
                .map(verificationTokenMapper::toDto);
    }

    @Override
    public Optional<VerificationTokenDto> findByUser(UserDto userDto) {
        log.info("Retrieving verification token by user email: {}", userDto);
        return verificationTokenRepository.findByUser(userMapper.toEntity(userDto))
                .map(verificationTokenMapper::toDto);
    }

    @Override
    public void delete(VerificationTokenDto token) {
        log.info("Deleting verification token, id: {}, token: {}, user id: {}",
                token.getId(), token.getToken(), token.getUser().getId());
        verificationTokenRepository.delete(verificationTokenMapper.toEntity(token));
    }

}
