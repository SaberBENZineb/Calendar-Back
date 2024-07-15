package com.example.db_calendar.controller;

import com.example.db_calendar.entity.User;
import com.example.db_calendar.service.user.UserResponse;
import com.example.db_calendar.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }
    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Integer userId)
    {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}
