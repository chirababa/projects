package com.example.springdemo.controller;

import com.example.springdemo.entities.User;
import com.example.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public UserController(UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping(value = "/{id}")
    public User findUserById(@PathVariable("id") Integer id){
        return userService.findUserById(id);
    }

    @GetMapping(value = "findUserByUsername/{username}")
    public User findUserByUsername(@PathVariable("username") String  username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/all")
    public List<User> findAll()
    {
        return userService.findAll();
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody User user) {
        return userService.insert(user);
    }

    @PutMapping("/update")
    public Integer update(@RequestBody User user) {

        return userService.update(user);
    }

    @DeleteMapping("/delete/{userVar}")
    public void delete(@PathVariable("userVar") Integer idUser){
        User user =  findUserById(idUser);
        userService.delete(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user)
    {
        return userService.login(user);
    }

    @PostMapping("/isDuplicate")
    public boolean isDuplicateCredentials(@RequestBody User user)
    {
        return userService.isDuplicateCredentials(user);
    }

    @PostMapping("/register")
    public Integer register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/addUserPatient")
    public Integer addUserPatient(@RequestBody User user) {
        return userService.addUserPatient(user);
    }

    @PostMapping("/addCaregiver")
    public Integer addCaregiver(@RequestBody User user) {
        return userService.addCaregiver(user);
    }

    @GetMapping("/userByRole/{roleName}")
    List<User> getAllUsersByRole (@PathVariable("roleName")String roleName)
    {
        return userService.getAllUsersByRole(roleName);
    }

    @MessageMapping("/message")
    public String alerting(String message,Integer idCaregiver) throws Exception{
        simpMessagingTemplate.convertAndSend("/topic/alert/" + idCaregiver,message);
        return message;
    }
}
