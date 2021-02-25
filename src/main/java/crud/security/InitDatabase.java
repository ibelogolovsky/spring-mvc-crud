package crud.security;

import crud.model.User;
import crud.service.UserService;
import crud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {

    private UserService userService;

    @Autowired
    public InitDatabase(UserService userService) {
        User jsmith = new User("jsmith", "1234",
                "John", "Smith", "jsmith@gmail.com");
        User admin = new User("admin", "admin",
                "Igor", "Belogolovsky", "ibelogolovsky@gmail.com");
        userService.addRole(jsmith, "ROLE_USER");
        userService.addRole(admin, "ROLE_ADMIN");
        userService.addRole(admin, "ROLE_USER");

    }



}
