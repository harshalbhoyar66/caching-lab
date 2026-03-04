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

    // READ → cache result
    @Cacheable(value = "users", key = "#id")
    public Customer getUser(Long id) {

        System.out.println("Fetching USER from DB");

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // CREATE → store user in DB
    public Customer createUser(String name, String email) {

        Customer user = Customer.builder()
                .name(name)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    // UPDATE → update cache
    @CachePut(value = "users", key = "#user.id")
    public Customer updateUser(Customer user) {

        return userRepository.save(user);
    }

    // DELETE → remove from cache
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}