package app.controller.service;

import app.model.dto.UserDTO;
import app.model.entity.Role;
import app.model.entity.User;
import app.model.repository.RoleRepository;
import app.model.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bcryptEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User save(UserDTO user) {
        if (userWithUsernameExists(user.getUsername()) || userWithEmailExists(user.getEmail()))
            return null;

        User newUser = new User();
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_ADMIN"));

        newUser.setLogin(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode((user.getPassword())));
        newUser.setEmail(user.getEmail());
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

    public boolean userWithUsernameExists(String username) {
        return userRepository.findUserByLogin(username).isPresent();
    }

    public boolean userWithEmailExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    public User getOneByLogin(String login) {
        return userRepository.findUserByLogin(login).orElse(null);
    }

    public User getOneByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<Role> getUsersRoles(String login) {
        return userRepository.getUsersRole(login);
    }
}
