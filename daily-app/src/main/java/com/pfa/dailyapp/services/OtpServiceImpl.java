package com.pfa.dailyapp.services;

import com.pfa.dailyapp.entities.Otp;
import com.pfa.dailyapp.exceptions.OtpNotFoundException;
import com.pfa.dailyapp.exceptions.UserNotFoundException;
import com.pfa.dailyapp.repositories.OtpRepository;
import com.pfa.dailyapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private OtpRepository otpRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private static final long OTP_EXPIRATION_MINUTES = 10;

    @Override
    public void saveOtp(String email, String otp) {
        try {
            if(userRepository.existsByEmail(email)) {
                Otp otp1;
                if(otpRepository.existsByEmail(email)) {
                    otp1 = otpRepository.findByEmail(email);
                } else {
                    otp1 = new Otp();
                    otp1.setEmail(email);
                }
                otp1.setOtpCode(passwordEncoder.encode(otp));
                otp1.setLocalDateTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
                otpRepository.save(otp1);
                log.info("OTP successfully saved for email: {}", email);
            } else {
              throw new UserNotFoundException("User not found !");
            }
        }catch (UserNotFoundException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        try {
            if(userRepository.existsByEmail(email)) {
                if(otpRepository.existsByEmail(email)) {
                    Otp otpStored = otpRepository.findByEmail(email);
                    if (LocalDateTime.now().isAfter(otpStored.getLocalDateTime())) {
                        otpRepository.delete(otpStored);
                        log.info("OTP has expired and has been deleted for email: {}", email);
                        return false;
                    } else {
                        boolean isValidOtp = passwordEncoder.matches(otp, otpStored.getOtpCode());
                        if (!isValidOtp) {
                            log.info("Invalid OTP for email: {}", email);
                        }
                        return isValidOtp;
                    }
                } else {
                    throw new OtpNotFoundException("OTP not found for the given email!");
                }
            } else {
                throw new UserNotFoundException("User not found !");
            }
        }catch (OtpNotFoundException | UserNotFoundException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void deleteOtp(String email) {
        otpRepository.deleteById(otpRepository.findByEmail(email).getOtpId());
    }

}
