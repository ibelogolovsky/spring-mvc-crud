package crud.controller;

import crud.model.Role;
import crud.service.RoleService;
import crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import crud.model.User;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    private final RoleService roleService;

    public UsersController(@Qualifier("userServiceRepoImpl") UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        roleService.save(new Role("ADMIN"));
        roleService.save(new Role("USER"));
        User jsmith = new User("jsmith", "1234",
                "John", "Smith", "jsmith@gmail.com");
        jsmith.addRole(roleService.findByRolename("USER"));
        userService.save(jsmith);
        User admin = new User(
                "admin", "admin",
                "Igor", "Belogolovsky", "ibelogolovsky@gmail.com");
        admin.addRole(this.roleService.findByRolename("ADMIN"));
        this.userService.save(admin);

    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.listAll());
        return "users/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.get(id));
        return "users/show";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.get(id));
        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        user.setId(id);
        userService.save(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/users";
    }
}
