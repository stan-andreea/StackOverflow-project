package com.utcn.demo;

import com.utcn.demo.controller.UserController;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
public class UserTests {

    @Autowired
    private UserController userController;


    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveUserTest(){

        User user = new User();
        user.setUsername("andreeaJunit2");
        user.setPassword("12343");
        user.setEmail("abcda");
        user.setFirstName("ada");
        user.setLastName("adfafa");
        user.setPoints(0);
        user.setRole(1);



        User newUser = userController.insertUser(user);

        Assertions.assertThat(!Objects.isNull(newUser));
    }

    @Test
    @Order(2)
    public void getUserTest(){
        User user = userController.getById(352L);
        Assertions.assertThat(user.getUserId()).isEqualTo(352L);
    }

    @Test
    @Order(3)
    public void getAllUsersTest(){
        List<User> users = userController.retrieveUsers();
        Assertions.assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateUserTest(){
        User user = userController.getById(352L);
        user.setEmail("testUpdate@gmail.com");

        User newUser =  userController.updateUser(52L,user);

        Assertions.assertThat(newUser.getEmail()).isEqualTo("testUpdate@gmail.com");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteUserTest(){

        User user = userController.getById(352L);
        userController.deleteById(352L);
        Optional<User> optionalUser = Optional.ofNullable(userController.getById(352L));

        Assertions.assertThat(Objects.isNull(optionalUser));
    }
}

