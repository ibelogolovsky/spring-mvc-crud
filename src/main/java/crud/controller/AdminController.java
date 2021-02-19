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
@RequestMapping("/admin")
public class AdminController {

    final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

//
//    @Autowired
//    private final RoleService roleService;

//    public AdminController(@Qualifier("userServiceRepoImpl") UserService userService, RoleService roleService) {
//        this.userService = userService;
//        this.roleService = roleService;
////        roleService.save(new Role("ROLE_USER"));
////        roleService.save(new Role("ROLE_ADMIN"));
//        User jsmith = new User("jsmith", "1234",
//                "John", "Smith", "jsmith@gmail.com");
//        jsmith.addRole(new Role("ROLE_USER"));
//        userService.save(jsmith);
//        User admin = new User(
//                "admin", "admin",
//                "Igor", "Belogolovsky", "ibelogolovsky@gmail.com");
//        admin.addRole(new Role("ROLE_ADMIN"));
////        admin.addRole(new Role("ROLE_USER"));
//        userService.save(admin);

//    }

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
    public String newUser(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.get(id));
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        user.setId(id);
        userService.save(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.remove(id);
        return "redirect:/admin";
    }

//    @GetMapping(value = "/login")
//    public String getLoginPage() {
//        return "login";
//    }
}
