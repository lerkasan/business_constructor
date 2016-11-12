package ua.com.brdo.business.constructor.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.entity.Role;

public interface RoleService {

    @Transactional
    Role create(Role role);

    @Transactional
    Role update(Role role);

    @Transactional
    void delete(Long id);

    Role findById(Long id);

    Optional<Role> findByTitle(String title);

    List<Role> findAll();
}
