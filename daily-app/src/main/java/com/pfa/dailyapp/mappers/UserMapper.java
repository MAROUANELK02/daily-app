package com.pfa.dailyapp.mappers;

import com.pfa.dailyapp.dtos.UserDTO;
import com.pfa.dailyapp.entities.User;
import com.pfa.dailyapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserMapper {
    private RoleMapper roleMapper;

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleDTOS(user.getRoles().stream().map(roleMapper::toRoleDTO)
                .collect(Collectors.toList()));
        return userDTO;
    }

    public User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setRoles(userDTO.getRoleDTOS().stream().map(roleMapper::toRole)
                .collect(Collectors.toList()));
        return user;
    }
}
