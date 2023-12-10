package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(
            UserService userService,
            RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getUsersList());
        return "admin";
    }

    @GetMapping("/id")
    public String show(@RequestParam(value = "id", required = false, defaultValue = "0")
                       Long id,
                       Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "/new";
    }

    @PostMapping
    public String create(User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/new";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/change/id")
    public String changeUser(
            Model model,
            @RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "/change";
    }

    @PostMapping("change/id")
    public String change(User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/change";
        }
        userService.editUser(user);
        return "redirect:/admin";
    }

    @PostMapping("delete/id")
    public String deleteUser(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
