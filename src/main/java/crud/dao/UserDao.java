package crud.dao;

import model.User;
import java.util.List;

public interface UserDao {
    void add(User user);
    User get(long id);
    void set(User user);
    void remove(long id);
    List<User> getAll();
}
