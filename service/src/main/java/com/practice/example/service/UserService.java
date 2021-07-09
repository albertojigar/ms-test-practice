package com.practice.example.service;


import com.practice.example.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements IUserService{

    public User createUser(final User user) {
        log.info("creating User {}", user.getName());
        user.setId(UUID.randomUUID().toString());
        return user;
    }

    public User getById(final String id) {
        log.info("retrieving user by Id {}", id);
        return User.builder().id(id)
                .email("test@practice.com")
                .lastName("Simpson")
                .name("Homero")
                .build();
    }
}
