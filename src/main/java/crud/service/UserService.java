package crud.service;

import model.User;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserService {
    void add(User user);
    User get(long id);
    void set(User user);
    void remove(long id);
    List<User> getAll();
}
