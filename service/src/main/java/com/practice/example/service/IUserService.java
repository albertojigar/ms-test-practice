package com.practice.example.service;


import com.practice.example.model.User;

public interface IUserService {
    User createUser(final User user);
    User getById(final String id);
}
