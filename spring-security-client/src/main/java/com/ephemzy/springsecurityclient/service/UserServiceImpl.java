package com.ephemzy.springsecurityclient.service;

import com.ephemzy.springsecurityclient.entity.PasswordResetToken;
import com.ephemzy.springsecurityclient.entity.User;
import com.ephemzy.springsecurityclient.entity.VerificationToken;
import com.ephemzy.springsecurityclient.model.UserModel;
import com.ephemzy.springsecurityclient.repository.PasswordResetRepository;
import com.ephemzy.springsecurityclient.repository.UserRepository;
import com.ephemzy.springsecurityclient.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setFirstname(userModel.getFirstname());
        user.setLastname(userModel.getLastname());
        user.setEmail(userModel.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if (verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        if (user.isEnabled()) {
            return "verified";
        }

        user.setEnabled(true);
        userRepository.save(user);
//        verificationTokenRepository.delete(verificationToken);

        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        log.info("UserServiceImpl My token is " + token);
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token);

        if (passwordResetToken == null) {
            log.info("UserServiceImpl passwordResetToken == null " + token);

            return "invalid";
        }

        User user = passwordResetToken.getUser();
        Calendar cal = Calendar.getInstance();

        if (passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0){
            log.info("UserServiceImpl passwordResetToken.getExpirationTime " + token);
            passwordResetRepository.delete(passwordResetToken);
            return "expired";
        }

        if (user.isEnabled()) {
            log.info("UserServiceImpl user.isEnabled " + token);
            return "verified";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
