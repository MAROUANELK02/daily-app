package com.pfa.dailyapp.repositories;

import com.pfa.dailyapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByFirstnameContainsOrLastnameContains(String firstname, String lastname, Pageable pageable);
    User findByUsernameOrEmail(String username, String email);
    Page<User> findAllByDeletedFalse(Pageable pageable);
    Boolean existsByUsername(String username);
}
