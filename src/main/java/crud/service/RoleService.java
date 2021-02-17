package crud.service;

import crud.dao.RoleRepository;
import crud.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository repo;

    public RoleService(RoleRepository repo) {
        this.repo = repo;
    }

    public void save(Role role) {
        repo.save(role);
    }

    public Role get(long id) {
        //noinspection OptionalGetWithoutIsPresent
        return repo.findById(id).get();
    }


    public void remove(long id) {
        repo.deleteById(id);
    }

    public List<Role> listAll() {
        return (List<Role>) repo.findAll();
    }

    public Role findByRolename(String rolename) {
        return repo.findByRolename(rolename);
    }
}
