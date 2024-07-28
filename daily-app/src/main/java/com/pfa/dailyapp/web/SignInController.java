package com.pfa.dailyapp.web;

import com.pfa.dailyapp.dtos.*;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.security.UserDetailsImpl;
import com.pfa.dailyapp.security.jwt.JwtUtils;
import com.pfa.dailyapp.security.mailing.EmailSenderService;
import com.pfa.dailyapp.services.OtpService;
import com.pfa.dailyapp.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SignInController {
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private OtpService otpService;
    private UserService userService;
    private EmailSenderService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Incorrect username or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("User with username: " + userDetails.getUsername() + " has logged in");
        return ResponseEntity.ok(new UserInfoResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam(name = "email") String email) {
        if (!userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email not found"));
        }

        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp);
        emailService.sendEmail(email, otp);

        return ResponseEntity.ok(new SuccessResponse("OTP sent to email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody OtpDTO request) {
        if (request.getPassword().equals(request.getConfirmedPassword())) {
            if (!otpService.validateOtp(request.getEmail(), request.getOtpCode())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid OTP"));
            }
            try {
                userService.resetPassword(request.getEmail(), request.getConfirmedPassword());
                otpService.deleteOtp(request.getEmail());
                return ResponseEntity.ok(new SuccessResponse("Password reset successful"));
            } catch (UserNotFoundException e) {
                log.error(e.getMessage());
                return ResponseEntity.badRequest().body(new ErrorResponse("User not Found !"));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("Passwords don't match"));
        }
    }

}
