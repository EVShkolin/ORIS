package ru.kpfu.repository;

import org.springframework.stereotype.Repository;
import ru.kpfu.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final AtomicLong counter = new AtomicLong();
    private List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return users;
    }

    public User findById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    public User save(User user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
        return user;
    }

    public void deleteById(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }
}
