package com.pfa.dailyapp.dtos;

import lombok.Data;

@Data
public class OtpDTO {
    private String email;
    private String otpCode;
    private String password;
    private String confirmedPassword;
}
