package com.pfa.dailyapp.repositories;

import com.pfa.dailyapp.entities.Role;
import com.pfa.dailyapp.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(ERole roleName);
}
