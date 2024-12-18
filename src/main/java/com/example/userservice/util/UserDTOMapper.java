package com.example.userservice.util;

import com.example.userservice.model.User;
import model.UserDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getName()
        );
    }
}
