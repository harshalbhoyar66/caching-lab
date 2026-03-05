package com.cachinglab.caching_lab.Service;

import com.cachinglab.caching_lab.entity.Order;
import com.cachinglab.caching_lab.repository.OrderRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final Cache<Long, Order> l1Cache =
            Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(10, TimeUnit.SECONDS)
                    .build();

    public Order getOrder(Long id) {

        Order order = l1Cache.getIfPresent(id);

        if (order != null) {
            System.out.println("L1 Cache Hit");
            return order;
        }

        order = (Order) redisTemplate.opsForValue().get("order:" + id);

        if (order != null) {
            System.out.println("L2 Redis Hit");

            l1Cache.put(id, order);
            return order;
        }

        System.out.println("DB Hit");

        order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        redisTemplate.opsForValue().set(
                "order:" + id,
                order,
                Duration.ofSeconds(60)
        );

        l1Cache.put(id, order);

        return order;
    }

    public Order createOrder(Order order) {

        Order saved = orderRepository.save(order);

        redisTemplate.opsForValue().set(
                "order:" + saved.getId(),
                saved,
                Duration.ofSeconds(60)
        );

        l1Cache.put(saved.getId(), saved);

        return saved;
    }

    public void deleteOrder(Long id) {

        orderRepository.deleteById(id);

        redisTemplate.delete("order:" + id);

        l1Cache.invalidate(id);
    }
}