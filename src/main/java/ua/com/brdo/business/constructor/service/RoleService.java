package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.entity.Role;

public interface RoleService {

    Role create(Role role);

    Role update(Role role);

    void delete(Long id);

    Role findById(Long id);

    Role findByTitle(String title);

    List<Role> findAll();
}
