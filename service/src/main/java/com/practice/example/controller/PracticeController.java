package com.practice.example.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.practice.example.model.Status;
import com.practice.example.model.User;
import com.practice.example.service.IUserService;
import com.practice.example.util.DefaultResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
public class PracticeController {

    private IUserService service;

    @PostMapping("/users")
    public ResponseEntity<Status<?>> createUser(@RequestBody User user) {
        try {
            User created = service.createUser(user);
            return DefaultResponseBuilder.defaultResponse(created, HttpStatus.CREATED);
        } catch (Exception ex) {
            return DefaultResponseBuilder.errorResponse(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    @HystrixCommand(fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<Status<?>> getUserById(@PathVariable String id) {
        try {
            User found = service.getById(id);
            if (found != null) {
                return DefaultResponseBuilder.defaultResponse(found, HttpStatus.OK);
            }
            else return DefaultResponseBuilder.errorResponse("User not found", null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return DefaultResponseBuilder.errorResponse(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Status<?>> getUserByIdFallback(final String id) {
        log.error("getting User by id fallback {}", id);
        Status<?> status = Status.builder()
                ._hits(1)
                .message("service unavailable. Registration getting User by id fallback id "+id+", please try again")
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .build();
        return new ResponseEntity<>(status, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
