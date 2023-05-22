package com.utcn.demo.controller;

import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping( "/user/getAll")
    @ResponseBody
    public List<User> retrieveUsers() {
        return userService.retrieveUsers();
    }


    @GetMapping("user/getById/{id}")
    @ResponseBody
    public User getById(@PathVariable Long id) {
        return  userService.retrieveUserById(id);
    }


    @GetMapping("user/isModerator/{id}")
    @ResponseBody
    public Boolean isModerator(@PathVariable Long id) {
        return  userService.isModerator(id);
    }


    @DeleteMapping("user/deleteById/{id}")
    public void deleteById(@PathVariable Long id) {userService.deleteById(id);}


    @PostMapping("/user/insert")
    public User insertUser(@RequestBody User user) {
        user.setDateJoined(Date.valueOf(LocalDate.now()));
        return userService.insertUser(user);
    }


    @PutMapping("/user/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);

        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    @GetMapping("/user/searchByUsername")
    public User getUserByUsernameAndPassword(@RequestParam("username") String username,
                                             @RequestParam("password") String password) {
        return userService.retrieveUserByUsernameAndPassword(username, password);
    }

    @PutMapping("user/ban/{userId}")
    public void banUser(@PathVariable Long userId) {
        userService.banUser(userId);

    }

    @GetMapping("user/isBanned/{userId}")
    public boolean isUserBanned(@PathVariable Long userId) {
        return userService.isUserBanned(userId);

    }




}



