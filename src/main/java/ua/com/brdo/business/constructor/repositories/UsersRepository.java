package ua.com.brdo.business.constructor.repositories;

import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
