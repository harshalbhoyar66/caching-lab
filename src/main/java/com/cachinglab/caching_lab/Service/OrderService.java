package com.cachinglab.caching_lab.Service;

import com.cachinglab.caching_lab.entity.Order;
import com.cachinglab.caching_lab.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Cacheable(value = "orders", key = "#id", sync = true)
    public Order getOrder(Long id) {

        System.out.println("Fetching ORDER from DB");

        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @CachePut(value = "orders", key = "#result.id")
    public Order createOrder(Order order) {

        return orderRepository.save(order);
    }

    @CacheEvict(value = "orders", key = "#id")
    public void deleteOrder(Long id) {

        orderRepository.deleteById(id);
    }
}