package app.model.repository;

import app.model.entity.Role;
import app.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT new Role(r.id, r.name) FROM User u JOIN u.roles r  WHERE u.login=?1")
    List<Role> getUsersRole(String login);
}
