package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.exception.UserNotFoundException;
import org.project.dto.FormDataDto;
import org.project.dto.UserDto;
import org.project.util.UserMapper;
import org.project.model.User;
import org.project.repository.UserRepository;
import org.project.util.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(mapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserDto save(FormDataDto formDataDto) {
        String passwordHash = encoder.encode(formDataDto.getPassword());
        User user = new User(formDataDto.getUsername(), passwordHash);
        user = userRepository.save(user);
        return mapper.convertToDto(user);
    }

    public boolean authenticate(FormDataDto formData) {
        User user = userRepository.findByUsername(formData.getUsername())
                .orElse(null);
        if (user == null) {
            return false;
        }
        return encoder.matches(formData.getPassword(), user.getPasswordHash());
    }
}
