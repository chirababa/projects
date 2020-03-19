package com.example.springdemo.services;

import com.example.springdemo.entities.Role;
import com.example.springdemo.entities.User;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.RoleRepository;
import com.example.springdemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findUserById(Integer id){
        Optional<User> user  = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", "user id", id);
        }
        return user.get();
    }

    public User getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Integer insert(User user) {
        return userRepository
                .save(user)
                .getIdUser();
    }

    public Integer update(User user) {

        Optional<User> userReturned = userRepository.findById(user.getIdUser());

        if(!userReturned.isPresent()){
            throw new ResourceNotFoundException("User", "user id", user.getIdUser().toString());
        }

        return userRepository.save(user).getIdUser();
    }

    public void delete(User user){
        this.userRepository.deleteById(user.getIdUser());
    }

    public User login(User user)
    {
        List<User> users = findAll();
        for(User u:users)
        {
            if(u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword()))
            {
                return u;
            }
        }
        return  null;
    }

    public boolean isDuplicateCredentials(User user)
    {
        boolean duplicateFlag = false;
        List<User> users = findAll();
        for(User u:users) {
            if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {
                duplicateFlag = true;
                break;
            }
        }
        return duplicateFlag;
    }

    public Integer register(User user) {
        System.out.println(user.getBirthdate());
        Role role = roleRepository.getRoleByName("doctor");
        user.setRole(role);
        this.insert(user);
        return user.getIdUser();
    }

    public Integer addUserPatient(User user) {
        Role role = roleRepository.getRoleByName("patient");
        user.setRole(role);
        this.insert(user);
        return user.getIdUser();
    }

    public Integer addCaregiver(User user) {
        Role role = roleRepository.getRoleByName("caregiver");
        user.setRole(role);
        this.insert(user);
        return user.getIdUser();
    }

    public List<User> getAllUsersByRole(String roleName)
    {
        return userRepository.getAllUsersByRole(roleName);
    }
}
