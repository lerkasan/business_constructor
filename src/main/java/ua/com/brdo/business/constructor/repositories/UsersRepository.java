package ua.com.brdo.business.constructor.repositories;

import ua.com.brdo.business.constructor.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
