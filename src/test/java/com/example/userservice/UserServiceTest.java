package com.example.userservice;

import com.example.userservice.model.User;
import com.example.userservice.persistence.UserRepository;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.AuthRequestDTO;
import model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateOrGetUser_existingUser() {
        UUID userId = UUID.randomUUID();
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(userId, "John Doe");
        User existingUser = User.builder().id(userId).name("John Doe").build();
        UserDTO expectedUserDTO = new UserDTO(userId, "John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(mapper.convertValue(existingUser, UserDTO.class)).thenReturn(expectedUserDTO);

        UserDTO result = userService.createOrGetUser(authRequestDTO);

        assertEquals(expectedUserDTO, result);
        verify(userRepository, times(1)).findById(userId);
        verify(mapper, times(1)).convertValue(existingUser, UserDTO.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testCreateOrGetUser_newUser() {
        UUID userId = UUID.randomUUID();
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(userId, "John Doe");
        User newUser = User.builder().id(userId).name("John Doe").build();
        UserDTO expectedUserDTO = new UserDTO(userId, "John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(mapper.convertValue(newUser, UserDTO.class)).thenReturn(expectedUserDTO);

        UserDTO result = userService.createOrGetUser(authRequestDTO);

        assertEquals(expectedUserDTO, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(newUser);
        verify(mapper, times(1)).convertValue(newUser, UserDTO.class);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = User.builder().id(UUID.randomUUID()).name("John Doe").build();
        User user2 = User.builder().id(UUID.randomUUID()).name("Jane Doe").build();
        List<User> users = List.of(user1, user2);
        List<UserDTO> expectedUserDTOs = List.of(
                new UserDTO(user1.getId(), user1.getName()),
                new UserDTO(user2.getId(), user2.getName())
        );

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.convertValue(user1, UserDTO.class)).thenReturn(expectedUserDTOs.get(0));
        when(mapper.convertValue(user2, UserDTO.class)).thenReturn(expectedUserDTOs.get(1));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(expectedUserDTOs, result);
        verify(userRepository, times(1)).findAll();
        verify(mapper, times(1)).convertValue(user1, UserDTO.class);
        verify(mapper, times(1)).convertValue(user2, UserDTO.class);
    }

    @Test
    public void testGetUserById_existingUser() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).name("John Doe").build();
        UserDTO expectedUserDTO = new UserDTO(userId, "John Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.convertValue(user, UserDTO.class)).thenReturn(expectedUserDTO);

        UserDTO result = userService.getUserById(userId);
        assertEquals(expectedUserDTO, result);
        verify(userRepository, times(1)).findById(userId);
        verify(mapper, times(1)).convertValue(user, UserDTO.class);
    }
}