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
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        userToUpdate.setName(user.getName());
        userToUpdate.setAge(user.getAge());
        userRepository.save(userToUpdate);
        return userToUpdate;
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
