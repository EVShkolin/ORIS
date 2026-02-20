package ru.kpfu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kpfu.model.User;
import ru.kpfu.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user, Long id) {
        User userToUpdate = userRepository.findById(id);
        userToUpdate.setName(user.getName());
        return userToUpdate;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
