package crud.service;

import crud.model.User;

import java.util.List;

public interface UserService {
    void add(User user);
    User get(long id);
    void set(User user);
    void remove(long id);
    List<User> getAll();
}
