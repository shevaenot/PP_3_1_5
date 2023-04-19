package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {

    private final UserService userService;

    public MyRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.listUsers(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/user-create")
    public ResponseEntity<User> create(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/user-delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/user-update/{id}")
    public ResponseEntity<Void> editUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
