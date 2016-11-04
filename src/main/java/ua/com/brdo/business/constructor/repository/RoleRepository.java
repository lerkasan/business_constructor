package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.brdo.business.constructor.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByTitle(String title);
}
