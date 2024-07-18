package com.pfa.dailyapp.mappers;

import com.pfa.dailyapp.dtos.RoleDTO;
import com.pfa.dailyapp.entities.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class RoleMapper {
    public RoleDTO toRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        return roleDTO;
    }

    public Role toRole(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return role;
    }
}
