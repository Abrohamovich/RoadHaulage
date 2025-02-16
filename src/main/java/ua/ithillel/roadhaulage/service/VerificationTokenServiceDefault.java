package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.mapper.VerificationTokenMapper;
import ua.ithillel.roadhaulage.repository.VerificationTokenRepository;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceDefault implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationTokenMapper verificationTokenMapper;

    @Override
    public VerificationTokenDto create(UserDto userDto, String token) {
        VerificationTokenDto verificationTokenDto = new VerificationTokenDto();
        verificationTokenDto.setToken(token);
        verificationTokenDto.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationTokenDto.setUser(userDto);
        return verificationTokenDto;
    }

    @Override
    public VerificationTokenDto save(VerificationTokenDto token) {
        VerificationToken saved = verificationTokenRepository
                .save(verificationTokenMapper.toEntity(token));
        return verificationTokenMapper.toDto(saved);
    }

    @Override
    public Optional<VerificationTokenDto> getToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .map(verificationTokenMapper::toDto);
    }

    @Override
    public void delete(VerificationTokenDto token) {
        verificationTokenRepository.delete(verificationTokenMapper.toEntity(token));
    }

}
