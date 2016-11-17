package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.entity.Role;

import java.util.List;

public interface RoleService {

    Role create(Role role);

    Role update(Role role);

    void delete(Long id);

    Role findById(Long id);

    Role findByTitle(String title);

    List<Role> findAll();
}
