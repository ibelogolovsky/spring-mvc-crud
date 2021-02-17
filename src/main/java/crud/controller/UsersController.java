package crud.controller;

import crud.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import crud.model.User;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(@Qualifier("userServiceRepoImpl") UserService userService) {
        this.userService = userService;
        this.userService.save(new User("jsmith", "1234", "John", "Smith", "jsmith@gmail.com"));
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
