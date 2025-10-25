package org.project.util;

import org.project.dto.UserDto;
import org.project.model.User;

public class UserMapper {

    public UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }

}
