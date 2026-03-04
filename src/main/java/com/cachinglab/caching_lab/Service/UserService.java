package com.cachinglab.caching_lab.service;

import com.cachinglab.caching_lab.cache.AdvancedCache;
import com.cachinglab.caching_lab.entity.User;
import com.cachinglab.caching_lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AdvancedCache<Long, User> cache =
            new AdvancedCache<>(100, 60_000);

    public User getUser(Long id) {

        User cached = cache.get(id);
        if (cached != null) {
            System.out.println("From CACHE");
            return cached;
        }

        System.out.println("From DB");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cache.put(id, user);
        return user;
    }

    public User createUser(String name, String email) {
        User user = User.builder()
                .name(name)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        cache.evict(id);
    }
}