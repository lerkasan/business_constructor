package ua.com.brdo.business.constructor.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

public interface RoleService {
    @Transactional
    Role create(Role role);

    @Transactional
    Role update(Role role);

    @Transactional
    void delete(Long id);

    boolean addUser(User user, Role role);

    boolean removeUser(User user, Role role);

    Role findById(Long id);

    Role findByTitle(String title);

    List<Role> findAll();
}
