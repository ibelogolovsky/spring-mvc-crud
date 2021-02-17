package crud.service;

import crud.dao.UserRepository;
import crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service()
public class UserServiceRepoImpl implements UserService {
    final UserRepository repo;

    public UserServiceRepoImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(User user) {
        repo.save(user);
    }

    @Override
    public User get(long id) {
        Optional<User> userFromDb = repo.findById(id);
        return userFromDb.orElse(new User());
    }

    @Override
    public void remove(long id) {
        repo.deleteById(id);
    }

    @Override
    public List<User> listAll() {
        return (List<User>) repo.findAll();
    }

    @Override
    public User findByLogin(String login) {
        return repo.findByLogin(login);
    }
}
