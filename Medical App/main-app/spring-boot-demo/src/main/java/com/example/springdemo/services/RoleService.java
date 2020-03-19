package com.example.springdemo.services;

import com.example.springdemo.entities.Role;
import com.example.springdemo.errorhandler.ResourceNotFoundException;
import com.example.springdemo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleById(Integer id){
        Optional<Role> role  = roleRepository.findById(id);

        if (!role.isPresent()) {
            throw new ResourceNotFoundException("Role", "role id", id);
        }
        return role.get();
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    public Integer insert(Role role) {
        return roleRepository
                .save(role).getIdRole();
    }

    public Integer update(Role role) {

        Optional<Role> roleReturned = roleRepository.findById(role.getIdRole());

        if(!roleReturned.isPresent()){
            throw new ResourceNotFoundException("Role", "role id", role.getIdRole().toString());
        }

        return roleRepository.save(role).getIdRole();
    }

    public void delete(Role role){
        this.roleRepository.deleteById(role.getIdRole());
    }

    public Role getRoleByName(String name)
    {
        return roleRepository.getRoleByName(name);
    }
}
