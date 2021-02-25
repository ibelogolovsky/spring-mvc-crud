package crud.controller;

import crud.model.Role;
import crud.service.RoleService;
import crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import crud.model.User;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.listAll());
        return "admin/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.get(id));
        return "admin/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user,
                          Model model) {
        model.addAttribute("allRoles", roleService.listAll());
        return "admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "roles") Role[] rolesArray) {
        User newUser = new User(user);
        for (Role role : rolesArray) {
            userService.addRole(newUser, role.getName());
        }
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.get(id));
        model.addAttribute("allRoles", roleService.listAll());
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "roles", required = false) Role[] rolesArray,
                         @PathVariable("id") long id) {
        user.setId(id);
        User dbUser = userService.get(id);
        user.setLogin(dbUser.getLogin());
        boolean passwordUpdated = true;
        if (user.getPassword().equals("")) {
            user.setPassword(dbUser.getPassword());
            passwordUpdated = false;
        }
        user.setRoles(new HashSet<>());
        userService.save(user, passwordUpdated);
        if (rolesArray != null) {
            for (Role role : rolesArray) {
                userService.addRole(user, role.getName());
            }
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/admin";
    }
}
