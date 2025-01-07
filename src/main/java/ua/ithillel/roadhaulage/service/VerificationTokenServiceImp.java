package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.repository.VerificationTokenRepository;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationTokenServiceImp implements VerificationTokenService {
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void save(VerificationToken token) {
        verificationTokenRepository.save(token);
    }

    @Override
    public Optional<VerificationToken> getToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

}
