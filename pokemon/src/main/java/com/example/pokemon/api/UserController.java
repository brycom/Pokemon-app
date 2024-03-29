package com.example.pokemon.api;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pokemon.model.User;
import com.example.pokemon.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUser() {
        return "hej";
    }

    @PostMapping("/newUser")
    public User newUser(@RequestBody User user) {

        Iterable<User> users = userRepository.findAll();
        ArrayList<String> userNameList = new ArrayList<String>();

        for (User u : users) {
            userNameList.add(u.getUsername());
        }

        if (userNameList.contains(user.getUsername())) {
            System.out.println(user.getUsername() + " finns redan.");
            return user;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

}
