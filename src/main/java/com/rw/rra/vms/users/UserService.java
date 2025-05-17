package com.rw.rra.vms.users;

import com.rw.rra.vms.common.exceptions.BadRequestException;
import com.rw.rra.vms.users.DTO.RegisterRequestDTO;
import com.rw.rra.vms.users.DTO.UserMapper;
import com.rw.rra.vms.users.DTO.UserResponseDTO;

import com.rw.rra.vms.vehicles.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.UUID;

@Service
@AllArgsConstructor

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponseDTO createUser(RegisterRequestDTO user) {
        if(userRepository.existsByEmailOrPhoneNumberOrNationalId(user.getEmail(), user.getPhoneNumber(), user.getNationalId()))
            throw new BadRequestException("User with this email or nationalId or phone number already exists.");

        var newUser = userMapper.toEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.ROLE_STANDARD);
        newUser.setEnabled(false);

        userRepository.save(newUser);
        log.info("Registered new user: {}", newUser.getEmail());

/*
        String otp = OtpUtil.generateOtp();
        //TODO: store OTP + expiry in cache or DB keyed by newUser.getEmail()
        emailService.sendAccountVerificationEmail(
                newUser.getEmail(),
                newUser.getFirstName(),
                otp
        );
*/

        return userMapper.toResponseDto(newUser);
    }

    public void changeUserPassword(String userEmail, String newPassword) {
        var user = findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void activateUserAccount(String userEmail) {
        var user = findByEmail(userEmail);
        user.setEnabled(true);
        userRepository.save(user);
    }

//    public void updateUserStatus(String email, Status newStatus) {
//        var user = findByEmail(email);
//        user.setStatus(newStatus);
//        userRepository.save(user);
//    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException("User with that email not found."));
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new BadRequestException("User is not authenticated.");
        }

        String userId = auth.getName(); // This is the user ID
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new BadRequestException("User with that ID not found."));
    }

    public UserResponseDTO getCurrentLoggedInUser() {
        return userMapper.toResponseDto(getAuthenticatedUser());
    }



}
