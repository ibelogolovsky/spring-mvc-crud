package crud.service;

import crud.dao.RoleRepository;
import crud.dao.UserRepository;
import crud.model.Role;
import crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository users;

    @Autowired
    private RoleRepository roles;

    private PasswordEncoder encoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public UserServiceImpl() {}

    @Override
    public void save(User user) {
        save(user, users.findByLogin(user.getLogin()).isEmpty());
    }

    @Override
    public void save(User user, boolean encodePassword) {
        if (encodePassword) {
            user.setPassword(encoder.encode(user.getPassword()));
        }
        users.save(user);
    }

    @Override
    public void setPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
    }

    @Override
    public User get(long id) {
        return users.findById(id).orElseGet(User::new);
    }

    @Override
    public void remove(long id) {
        users.deleteById(id);
    }

    @Override
    public List<User> listAll() {
        return (List<User>) users.findAll();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return findByLogin(login).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User %s not found", login))
        );
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(
                        role -> new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }

    @Override
    public void addRole(User user, String roleName) {
        Role role = roles.findByName(roleName).orElseGet(() -> new Role(roleName));
        user.getRoles().add(role);
        save(user);
    }
}
