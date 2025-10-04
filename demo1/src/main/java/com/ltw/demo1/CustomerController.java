package com.ltw.demo1;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Security 6!";
    }

    @GetMapping("/customers")
    public List<Customer> getCustomerList() {
        return List.of(
                new Customer(1, "Nguyen Thi Ngoc Tram"),
                new Customer(2, "Tran Nguyen AB")
        );
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to Demo1 Application!<br>" +
               "<a href='/hello'>Go to Hello</a><br>" +
               "<a href='/customers'>Go to Customers (requires login)</a>";
    }
}
