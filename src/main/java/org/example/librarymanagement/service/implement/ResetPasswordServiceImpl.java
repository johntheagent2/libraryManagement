package org.example.librarymanagement.service.implement;

import lombok.AllArgsConstructor;
import org.example.librarymanagement.dto.request.ResetPasswordRequest;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.ResetPasswordSession;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.ResetPasswordRepository;
import org.example.librarymanagement.service.ResetPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final ResetPasswordRepository passwordRepository;
    private final ResourceBundle resourceBundle;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public void requestChangePassword(AppUser appUser, ResetPasswordRequest request){
        checkIfEmailRequestBefore(request.getEmail());
        savePasswordSession(appUser, request);

    }

    @Override
    public void savePasswordSession(AppUser appUser, ResetPasswordRequest request){
        passwordRepository.save(new ResetPasswordSession(
                UUID.randomUUID().toString(),
                passwordEncoder.encode(request.getPassword()),
                LocalDateTime.now().plusMinutes(5),
                appUser
        ));
    }

    @Override
    @Transactional
    public AppUser confirmToken(String token){
        ResetPasswordSession resetPasswordSession = getResetPasswordSessionOptional(token);
        AppUser appUser;

        verifyExpiryDate(resetPasswordSession);
        appUser = resetPasswordSession.getAppUser();
        appUser.setPassword(resetPasswordSession.getNewPassword());
        deletePasswordSession(resetPasswordSession.getId());
        return appUser;
    }

    @Override
    public void verifyExpiryDate(ResetPasswordSession resetPasswordSession){
        if(resetPasswordSession.getExpirationDate().isBefore(LocalDateTime.now())){
            deletePasswordSession(resetPasswordSession.getId());
            throw new NotFoundException("confirmation-token.link.link-expired",
                    resourceBundle.getString("confirmation-token.link.link-expired"));
        }
    }

    @Override
    public void deletePasswordSession(Long id){
        passwordRepository.deleteById(id);
    }

    @Override
    public void checkIfEmailRequestBefore(String email){
        passwordRepository.findByAppUser_Email(email)
                .ifPresent(session -> deletePasswordSession(session.getId()));
    }

    @Override
    public ResetPasswordSession getResetPasswordSessionOptional(String token){
        return passwordRepository.findResetPasswordSessionByToken(token)
                .orElseThrow(() -> new NotFoundException("confirmation-token.link.link-not-found",
                        resourceBundle.getString("confirmation-token.link.link-not-found")));
    }
}
