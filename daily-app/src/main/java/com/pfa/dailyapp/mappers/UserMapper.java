package com.pfa.dailyapp.mappers;

import com.pfa.dailyapp.dtos.UserDTORequest;
import com.pfa.dailyapp.dtos.UserDTOResponse;
import com.pfa.dailyapp.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserMapper {
    private RoleMapper roleMapper;

    public UserDTOResponse toUserDTO(User user) {
        UserDTOResponse userDTOResponse = new UserDTOResponse();
        BeanUtils.copyProperties(user, userDTOResponse);
        userDTOResponse.setRoleDTOS(user.getRoles().stream().map(roleMapper::toRoleDTO)
                .collect(Collectors.toList()));
        return userDTOResponse;
    }

    public User toUser(UserDTOResponse userDTOResponse) {
        User user = new User();
        BeanUtils.copyProperties(userDTOResponse, user);
        user.setRoles(userDTOResponse.getRoleDTOS().stream().map(roleMapper::toRole)
                .collect(Collectors.toList()));
        return user;
    }

    public User toUser(UserDTORequest userDTORequest) {
        User user = new User();
        BeanUtils.copyProperties(userDTORequest, user);
        return user;
    }

}
