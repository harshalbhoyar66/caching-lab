package com.cachinglab.caching_lab.Controller;

import com.cachinglab.caching_lab.entity.Customer;
import com.cachinglab.caching_lab.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE USER
    @PostMapping
    public Customer createUser(@RequestParam String name,
                               @RequestParam String email) {

        return userService.createUser(name, email);
    }

    // GET USER
    @GetMapping("/{id}")
    public Customer getUser(@PathVariable Long id) {

        return userService.getUser(id);
    }

    // UPDATE USER
    @PutMapping
    public Customer updateUser(@RequestBody Customer user) {

        return userService.updateUser(user);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
    }
}