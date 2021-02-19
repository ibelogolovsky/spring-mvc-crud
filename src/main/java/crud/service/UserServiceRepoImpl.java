package crud.service;

import crud.dao.RoleRepository;
import crud.dao.UserRepository;
import crud.model.Role;
import crud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceRepoImpl implements UserService {
    private final UserRepository users;

    private final RoleRepository roles;

    public UserServiceRepoImpl(UserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    @Override
    public void save(User user) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (users.findByLogin(user.getLogin()).isEmpty()) {
            user.setPassword(encoder.encode(user.getPassword()));
            users.save(user);
        }
    }

    @Override
    public User get(long id) {
        Optional<User> userFromDb = users.findById(id);
        return userFromDb.orElse(new User());
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
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = findByLogin(login).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User %s not found", login))
        );
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                rolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(
                        role -> new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addRole(User user, String roleName) {
        Optional<Role> roleOptional = roles.findByName(roleName);
        Role role = roleOptional.orElseGet(() -> new Role(roleName));
        Optional<User> userOptional = users.findById(user.getId());
        if (!userOptional.isPresent()) {
            users.save(user);
        }
        user.getRoles().add(role);
        users.save(user);
    }
}
