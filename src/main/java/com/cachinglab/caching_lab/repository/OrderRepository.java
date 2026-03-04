package com.cachinglab.caching_lab.repository;

import com.cachinglab.caching_lab.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}