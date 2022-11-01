package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    private final UserServiceImpl userService;


    public MainController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("admin")
    public String listUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("user")
    public String infoUser(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "create")
    public String newUser(User user, Model model) {
        model.addAttribute(user);
        model.addAttribute("roles", userService.listRoles());
        return "create";
    }


    @PostMapping(value = "create")
    public String newUser(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("roles", userService.listRoles());
        if (bindingResult.hasErrors()){
            return "create";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("username: \"%s\" занят!", user.getUsername())));
            return "create";
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", userService.listRoles());

        return "update";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("roles", userService.listRoles());
        if (bindingResult.hasErrors()){
            return "update";
        }
        User userFromDB = userService.findByUsername(user.getUsername());
        if (userFromDB.getId()!=user.getId() && userFromDB != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("username: \"%s\" занят!", user.getUsername())));
            return "update";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}