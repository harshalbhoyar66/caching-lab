package com.cachinglab.caching_lab.Service;

import com.cachinglab.caching_lab.entity.Customer;
import com.cachinglab.caching_lab.repository.UserRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // L1 Cache (10 seconds TTL)
    private final Cache<Long, Customer> l1Cache =
            Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(10, TimeUnit.SECONDS)
                    .build();

    public Customer getUser(Long id) {

        // L1 cache
        Customer user = l1Cache.getIfPresent(id);

        if (user != null) {
            System.out.println("L1 Cache Hit");
            return user;
        }

        // L2 Redis
        user = (Customer) redisTemplate.opsForValue().get("user:" + id);

        if (user != null) {
            System.out.println("L2 Redis Hit");

            l1Cache.put(id, user);
            return user;
        }

        // DB
        System.out.println("DB Hit");

        user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        redisTemplate.opsForValue().set(
                "user:" + id,
                user,
                Duration.ofSeconds(60)
        );

        l1Cache.put(id, user);

        return user;
    }

    public Customer createUser(String name, String email) {

        Customer user = Customer.builder()
                .name(name)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();

        Customer saved = userRepository.save(user);

        redisTemplate.opsForValue().set(
                "user:" + saved.getId(),
                saved,
                Duration.ofSeconds(60)
        );

        l1Cache.put(saved.getId(), saved);

        return saved;
    }

    public Customer updateUser(Customer user) {

        Customer updated = userRepository.save(user);

        redisTemplate.opsForValue().set(
                "user:" + updated.getId(),
                updated,
                Duration.ofSeconds(60)
        );

        l1Cache.put(updated.getId(), updated);

        return updated;
    }

    public void deleteUser(Long id) {

        userRepository.deleteById(id);

        redisTemplate.delete("user:" + id);

        l1Cache.invalidate(id);
    }
}