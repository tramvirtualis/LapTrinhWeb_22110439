package com.ltw.demo3;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final UserInfoRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(UserInfoRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, guest!";
    }

    @GetMapping("/customers")
    public List<Customer> getCustomerList() {
        return List.of(
                new Customer(1, "Nguyen Van A"),
                new Customer(2, "Tran Thi B")
        );
    }

    @PostMapping("/new")
    public String addUser(@RequestBody UserInfo user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User added!";
    }
}
