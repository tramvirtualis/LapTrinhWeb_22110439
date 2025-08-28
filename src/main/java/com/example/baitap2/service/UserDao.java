package com.example.baitap2.service;

import com.example.baitap2.model.User;

public interface UserDao {
    User get(String username);
}
