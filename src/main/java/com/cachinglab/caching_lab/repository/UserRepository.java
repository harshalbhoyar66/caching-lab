package com.cachinglab.caching_lab.repository;

import com.cachinglab.caching_lab.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Customer, Long> {
}