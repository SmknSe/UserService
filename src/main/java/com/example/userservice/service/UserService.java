package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.persistence.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import model.AuthRequestDTO;
import model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserDTO createOrGetUser(AuthRequestDTO authRequestDTO) {
        Optional<User> existingUser = userRepository.findById(authRequestDTO.id());

        if (existingUser.isPresent()) {
            return existingUser.map((user) -> objectMapper.convertValue(user, UserDTO.class)).orElseThrow(EntityNotFoundException::new);
        }

        var savedUser = userRepository.save(
                User.builder()
                        .id(authRequestDTO.id())
                        .name(authRequestDTO.name())
                        .build()
        );
        return objectMapper.convertValue(savedUser, UserDTO.class);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map((user) -> objectMapper.convertValue(user, UserDTO.class)).toList();
    }

    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id).map((user) -> objectMapper.convertValue(user, UserDTO.class)).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}