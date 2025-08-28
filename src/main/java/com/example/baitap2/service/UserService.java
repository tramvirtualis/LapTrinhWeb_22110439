package com.example.baitap2.service;

import com.example.baitap2.model.User;

public interface UserService {
    User login(String username, String password);
    User get(String username);
}
