package com.example.springdemo.controller;

import com.example.springdemo.entities.Role;
import com.example.springdemo.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/{id}")
    public Role findRoleById(@PathVariable("id") Integer id){
        return roleService.findRoleById(id);
    }

    @GetMapping("/all")
    public List<Role> findAll(){
        return roleService.findAll();
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody Role role) {
        return roleService.insert(role);
    }

    @PutMapping("/update")
    public Integer update(@RequestBody Role role) {
        return roleService.update(role);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestBody  Role role){
        roleService.delete(role);
    }

    @GetMapping(path="/userRole/{name}")
    public @ResponseBody
    Role getRoleByName(@PathVariable("name") String name)
    {
        return roleService.getRoleByName(name);
    }
}
