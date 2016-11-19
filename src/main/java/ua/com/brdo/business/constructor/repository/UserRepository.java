package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

   /*@Query("SELECT CASE WHEN EXISTS (SELECT u FROM User WHERE u.email = :email) " +
           "THEN TRUE ELSE FALSE END")*/
    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN FALSE ELSE TRUE END " +
            "FROM User u WHERE UPPER(u.email) = UPPER(:email)")
    boolean availableEmail(@Param("email")String email);
}
