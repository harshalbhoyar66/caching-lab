package com.cachinglab.caching_lab.Service;

import com.cachinglab.caching_lab.entity.Customer;
import com.cachinglab.caching_lab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#id", sync = true)
    public Customer getUser(Long id) {

        System.out.println("Fetching USER from DB");

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Customer createUser(String name, String email) {

        Customer user = Customer.builder()
                .name(name)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @CachePut(value = "users", key = "#user.id")
    public Customer updateUser( Customer user) {

        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}