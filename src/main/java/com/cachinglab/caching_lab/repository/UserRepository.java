package com.cachinglab.caching_lab.repository;

import com.cachinglab.caching_lab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}