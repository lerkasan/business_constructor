package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Role;

public interface RoleService {

    Role create(Role role);

    Role update(Role role);

    void delete(long id);

    Role findById(long id);

    Role findByTitle(String title);

    List<Role> findAll();
}
