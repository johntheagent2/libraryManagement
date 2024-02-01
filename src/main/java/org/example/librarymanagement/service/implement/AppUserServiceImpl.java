package org.example.librarymanagement.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.*;
import org.example.librarymanagement.dto.response.MfaResponse;
import org.example.librarymanagement.dto.response.UserResponse;
import org.example.librarymanagement.entity.AppUser;
import org.example.librarymanagement.entity.TokenOTP;
import org.example.librarymanagement.enumeration.AccountStatus;
import org.example.librarymanagement.enumeration.ChangeType;
import org.example.librarymanagement.enumeration.Role;
import org.example.librarymanagement.exception.exception.BadCredentialException;
import org.example.librarymanagement.exception.exception.BadRequestException;
import org.example.librarymanagement.exception.exception.NotFoundException;
import org.example.librarymanagement.repository.AppUserRepository;
import org.example.librarymanagement.service.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final ResourceBundle resourceBundle;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleAuthenticatorService googleAuthenticatorService;
    private final TokenOtpService tokenOtpService;
    private final SessionService sessionService;
    private final AccountService accountService;

    private final ChangeType resetPassword = ChangeType.RESET_PASSWORD;
    private final ChangeType changeEmail = ChangeType.CHANGE_EMAIL;
    private final ChangeType changePhoneNumber = ChangeType.CHANGE_PHONE_NUMBER;

    @Override
    public void editUser(EditUserInfoRequest request, Long id) {
        AppUser appUser;

        if(findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException("user.email.email-existed",
                    resourceBundle.getString("user.email.email-existed"));
        }

        appUser = getById(id);
        appUser = updateUserInfo(request, appUser);
        updateUser(appUser);
    }

    private AppUser updateUserInfo(EditUserInfoRequest request, AppUser appUser){
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        appUser.setAddress(request.getAddress());
        appUser.setEmail(request.getEmail());
        appUser.setPhoneNumber(request.getPhoneNumber());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setStatus(request.getStatus());
        appUser.setCountWrongLogin(request.getCountWrongLogin());

        return appUser;
    }

    public AppUser getById(Long id){
        return appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.account.id-not-found",
                        resourceBundle.getString("user.account.id-not-found")));
    }

    @Override
    public void verifyUser(AppUser user){
        user.setStatus(AccountStatus.ACTIVE);
        updateUser(user);
    }

    @Override
    public void createUser(AdminCreateUserRequest request) {
        AppUser appUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .mfa(false)
                .status(request.getStatus())
                .enabled(request.getStatus().equals(AccountStatus.ACTIVE))
                .build();
        saveUser(appUser);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findAppUserByEmail(email);
    }

    @Override
    public void deleteUser(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.account.id-not-found",
                        resourceBundle.getString("user.account.id-not-found")));

        appUser.setEnabled(false);
        updateUser(appUser);
    }

    @Override
    @Transactional
    public void requestResetPassword(ResetPasswordRequest request) {
        AppUser appUser = getAppUser(request.getEmail());
        if(!accountService.checkMatchingPassword(request, appUser.getPassword())){
            tokenOtpService.deleteDuplicateRequest(resetPassword, request.getPassword());
            tokenOtpService.saveOtp(request.getPassword(), appUser, resetPassword);
        }else{
            throw new BadCredentialException("user.password.existed",
                    resourceBundle.getString("user.password.existed"));
        }
    }

    @Override
    public MfaResponse enableUserMfa() {
        String email = getCurrentLogin().getUsername();
        String secretKey = googleAuthenticatorService.generateSecretKey();
        return new MfaResponse(secretKey, googleAuthenticatorService.generateQRCode(email, secretKey));
    }

    @Override
    public void confirmMFA(String secretKey, String otp) {
        AppUser appUser = getAppUser(getCurrentLogin().getUsername());
        googleAuthenticatorService.validateSecretKey(secretKey, Integer.parseInt(otp));
        appUser.setMfa(true);
        appUser.setSecretKey(secretKey);
        updateUser(appUser);
    }

    @Override
    public boolean isUserEnableMfa(String email){
        AppUser appUser = getAppUser(email);
        return appUser.getMfa();
    }

    @Override
    public boolean validateMfaOtp(String email, int otp) {
        AppUser appUser = getAppUser(email);
        return googleAuthenticatorService.validateTOTP(appUser, otp);
    }

    @Override
    public AppUser getAppUser(String email){
        return findByEmail(email)
                .orElseThrow(() -> new BadRequestException("user.email.email-not-found",
                        resourceBundle.getString("user.email.email-not-found")));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return appUserRepository.findAll()
                .stream()
                .map((appUser -> UserResponse.builder()
                        .firstName(appUser.getFirstName())
                        .lastName(appUser.getLastName())
                        .address(appUser.getAddress())
                        .email(appUser.getEmail())
                        .phoneNumber(appUser.getPhoneNumber())
                        .address(appUser.getAddress())
                        .build()))
                .toList();
    }

    @Override
    public void saveUser(AppUser appUser){
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
    }

    @Override
    public void updateUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Transactional
    @Override
    public void requestChangePhoneNumber(ChangePhoneNumberRequest request) {
        String email = getCurrentLogin().getUsername();
        AppUser appUser = getAppUser(email);
        String currentPhoneNumber = appUser.getPhoneNumber();

        if(currentPhoneNumber.equals(request.getPhoneNumber())){
            throw new BadRequestException(resourceBundle.getString("user.phone.phone-duplicate"),
                    "user.phone.phone-duplicate");
        }
        tokenOtpService.deleteDuplicateRequest(changePhoneNumber, request.getPhoneNumber());
        tokenOtpService.saveOtp(request.getPhoneNumber(), appUser, changePhoneNumber);
    }

    @Transactional
    @Override
    public void requestChangeEmail(ChangeEmailRequest request) {
        String email = getCurrentLogin().getUsername();
        AppUser appUser;
        if(email.equals(request.getEmail())){
            throw new BadRequestException(resourceBundle.getString("user.email.email-duplicate"),
                    "user.email.email-duplicate");
        }

        if(findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException("user.email.email-existed",
                    resourceBundle.getString("user.email.email-existed"));
        }

        appUser = getAppUser(email);
        tokenOtpService.deleteDuplicateRequest(changeEmail, request.getEmail());
        tokenOtpService.saveOtp(request.getEmail(), appUser, changeEmail);

    }

    @Override
    public UserDetails getCurrentLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new BadRequestException(resourceBundle.getString("security.core.userdetails"), "security.core.userdetails");
        }
    }
}
