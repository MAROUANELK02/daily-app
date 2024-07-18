package com.pfa.dailyapp.dtos;

import com.pfa.dailyapp.enums.ERole;
import lombok.Data;

@Data
public class RoleDTO {
    private Long roleId;
    private ERole roleName;
}
