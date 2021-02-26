package crud.dao;

import crud.model.User;

import java.util.List;
//import org.springframework.data.repository.CrudRepository;

public interface UserRepository {
    User findById(long id);

    void save(User user);

    void deleteById(long id);

    List<User> findAll();
}
