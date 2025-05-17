package com.rw.rra.vms.auth;


//import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import com.rw.rra.vms.auth.DTO.*;
import com.rw.rra.vms.common.exceptions.BadRequestException;
import com.rw.rra.vms.email.EmailService;
import com.rw.rra.vms.users.DTO.RegisterRequestDTO;
import com.rw.rra.vms.users.DTO.UserResponseDTO;
import com.rw.rra.vms.users.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;



    @PostMapping("/register")
//    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody
                                                        RegisterRequestDTO user, UriComponentsBuilder uriBuilder){
        var userResponse = userService.createUser(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userResponse.getId()).toUri();
        // Use otp service to send otp to a registered user
        var otpToSend = otpService.generateOtp(userResponse.getEmail(), OtpType.VERIFY_ACCOUNT);

        // Send email to the user with the OTP
        emailService.sendAccountVerificationEmail(userResponse.getEmail(), userResponse.getFirstName(), otpToSend);
        return ResponseEntity.created(uri).body(userResponse);
    }

    @PatchMapping("/verify-account")
//    @RateLimiter(name = "otp-rate-limiter")
    ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyAccountDTO verifyAccountRequest){
        if (!otpService.verifyOtp(verifyAccountRequest.getEmail(), verifyAccountRequest.getOtp(), OtpType.VERIFY_ACCOUNT))
            throw new BadRequestException("Invalid email or OTP");

        userService.activateUserAccount(verifyAccountRequest.getEmail());

        var user = userService.findByEmail(verifyAccountRequest.getEmail());
//        userService.updateUserStatus(user.getEmail(), Status.ACTIVE);

        emailService.sendVerificationSuccessEmail(user.getEmail(), user.getFirstName());

        return ResponseEntity.ok("Account Activated successfully");
    }


    @PostMapping("/initiate-password-reset")
    ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody InitiatePassResetDTO initiateRequest){
        var otpToSend = otpService.generateOtp(initiateRequest.getEmail(), OtpType.FORGOT_PASSWORD);
        var user = userService.findByEmail(initiateRequest.getEmail());
//        userService.updateUserStatus(user.getEmail(), Status.RESET);
        emailService.sendResetPasswordOtp(user.getEmail(), user.getFirstName(), otpToSend);
        return ResponseEntity.ok("If your email is registered, you will receive an email with instructions to reset your password.");
    }



    @PatchMapping("/reset-password")
//    @RateLimiter(name = "auth-rate-limiter")
    ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordRequest){
        if (!otpService.verifyOtp(resetPasswordRequest.getEmail(), resetPasswordRequest.getOtp(), OtpType.FORGOT_PASSWORD)) {
            throw new BadRequestException("Invalid email or OTP");
        }

        // Change password
        userService.changeUserPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNewPassword());

        // Fetch user
        var user = userService.findByEmail(resetPasswordRequest.getEmail());

//        userService.updateUserStatus(user.getEmail(), Status.ACTIVE);


        // Send success email
        emailService.sendResetPasswordSuccessEmail(user.getEmail(), user.getFirstName());

        return ResponseEntity.ok("Password reset went successfully. You can login with your new password.");
    }


    @PostMapping("/login")
//    @RateLimiter(name = "auth-rate-limiter")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDto, HttpServletResponse response) {
        var loginResult = authService.login(loginRequestDto, response);
        return ResponseEntity.ok(new LoginResponse(loginResult.getAccessToken()));
    }



}
