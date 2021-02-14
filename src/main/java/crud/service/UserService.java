package crud.service;

import crud.model.User;

import java.util.List;

public interface UserService {
    void save(User user);
    User get(long id);
    void remove(long id);
    List<User> listAll();
}
