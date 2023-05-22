package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;



    public List<User> retrieveUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User retrieveUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        else {
            return null;
        }

    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User insertUser(User user) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsBanned(0);
        return userRepository.save(user);
    }



    public User retrieveUserByUsernameAndPassword(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        System.out.println(password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("found user");

            if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                return user;
            }
        }
        System.out.println("not found");
        return null;
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setPoints(updatedUser.getPoints());
            user.setEmail(updatedUser.getEmail());
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());

            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public void banUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsBanned(1);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found.");
        }
    }
    public boolean isUserBanned(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.getIsBanned()==1)
                return true;
        }
        return false;
    }

    public boolean isModerator(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            System.out.println(user);
            System.out.println(user.getRole());
            return user != null && user.getRole()==0;
        }
        return false;
    }






}
