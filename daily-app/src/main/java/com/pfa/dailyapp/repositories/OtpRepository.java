package com.pfa.dailyapp.repositories;

import com.pfa.dailyapp.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByEmail(String email);
    boolean existsByEmail(String email);
}
