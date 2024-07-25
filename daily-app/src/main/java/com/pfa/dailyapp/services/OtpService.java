package com.pfa.dailyapp.services;

public interface OtpService {
    void saveOtp(String email, String otp);
    boolean validateOtp(String email, String otp);
    String generateOtp();
    void deleteOtp(String email);
}
