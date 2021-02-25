package crud.service;

import crud.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    @Transactional
    void save(User user);

    void save(User user, boolean encodePassword);

    void setPassword(User user, String password);

    User get(long id);

    @Transactional
    void remove(long id);

    List<User> listAll();

    Optional<User> findByLogin(String login);

    @Transactional
    void addRole(User user, String roleName);
}
