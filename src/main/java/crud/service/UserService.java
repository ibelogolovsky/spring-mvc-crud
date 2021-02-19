package crud.service;

import crud.model.Role;
import crud.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void save(User user);
    User get(long id);
    void remove(long id);
    List<User> listAll();
    Optional<User> findByLogin(String login);

    @Transactional
    void addRole(User user, String roleName);
}
