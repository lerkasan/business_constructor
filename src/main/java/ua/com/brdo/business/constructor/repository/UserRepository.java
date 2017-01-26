package ua.com.brdo.business.constructor.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOne(long id);

    Optional<User> findByUsername(String username);

    @Query("SELECT count(u) = 0 FROM User u WHERE LOWER(u.email) = LOWER(?)")
    boolean emailAvailable(String email);

    @Query("SELECT count(u) = 0 FROM User u WHERE LOWER(u.email) = LOWER(:email) AND id != :id")
    boolean emailAvailable(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT count(u) = 0 FROM User u WHERE LOWER(u.username) = LOWER(?)")
    boolean usernameAvailable(String username);

    @Query("SELECT count(u) = 0 FROM User u WHERE LOWER(u.username) = LOWER(:email) AND id != :id")
    boolean usernameAvailable(@Param("email") String username, @Param("id") Long id);
}
