package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;


@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(@RequestParam(value = "id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }

    @GetMapping("/create")
    public String creator(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(name = "ROLE_USER", defaultValue = "false") boolean userRole,
                         @RequestParam(name = "ROLE_ADMIN", defaultValue = "false") boolean adminRole) {
        if (bindingResult.hasErrors()) {
            return "new";
        }

        if (userRole) {
            user.addRole(userService.getRoleByName("ROLE_USER"));
        }
        if (adminRole) {
            user.addRole(userService.getRoleByName("ROLE_ADMIN"));
        }
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editor(@RequestParam(value = "id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        for (Role role : userService.getUserById(id).getRoles()) {
            if ("ROLE_USER".equals(role.getName())) {
                model.addAttribute("userRole", true);
            }
            if ("ROLE_ADMIN".equals(role.getName())) {
                model.addAttribute("adminRole", true);
            }
        }
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult,
                       @RequestParam(value = "id") long id,
                       @RequestParam(name = "ROLE_USER", defaultValue = "false") boolean userRole,
                       @RequestParam(name = "ROLE_ADMIN", defaultValue = "false") boolean adminRole) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        if (userRole) {
            user.addRole(userService.getRoleByName("ROLE_USER"));
        }
        if (adminRole) {
            user.addRole(userService.getRoleByName("ROLE_ADMIN"));
        }
        userService.editUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}




