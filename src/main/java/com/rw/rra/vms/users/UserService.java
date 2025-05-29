package com.rw.rra.vms.users;

import com.rw.rra.vms.common.exceptions.BadRequestException;
import com.rw.rra.vms.users.DTO.RegisterRequestDTO;
import com.rw.rra.vms.users.DTO.UserMapper;
import com.rw.rra.vms.users.DTO.UserResponseDTO;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
//    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserResponseDTO createUser(RegisterRequestDTO user) {
        if(userRepository.existsByEmailOrPhoneNumberOrNationalId(user.getEmail(), user.getPhoneNumber(), user.getNationalId()))
            throw new BadRequestException("User with this email or nationalId or phone number already exists.");

        var newUser = userMapper.toEntity(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole()); // Use the role from RegisterRequestDTO
        newUser.setEnabled(false);
        newUser.setStatus(UserStatus.ACTIVE); // Set status to ACTIVE by default

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

    public void updateUserStatus(String email, UserStatus newStatus) {
        var user = findByEmail(email);
        user.setStatus(newStatus);
        userRepository.save(user);
    }

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

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable)
                .map(userMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByStatus(UserStatus status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable)
                .map(userMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsersByName(String searchTerm, Pageable pageable) {
        return userRepository.findByFirstNameContainingOrLastNameContaining(searchTerm, searchTerm, pageable)
                .map(userMapper::toResponseDto);
    }



}
