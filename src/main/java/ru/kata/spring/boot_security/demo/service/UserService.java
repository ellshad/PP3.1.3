package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(Long id);
    List<Role> listRoles();
    List<User> getAllUsers();
    void saveUser(User user);
    void saveRole(Role role);
    User findByUsername(String username);
    void removeUserById(Long id);
}
