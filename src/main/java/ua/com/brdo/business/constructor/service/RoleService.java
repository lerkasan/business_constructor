package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.model.Role;

import java.util.List;

public interface RoleService {

    Role create(Role role);

    Role update(Role role);

    void delete(long id);

    Role findById(long id);

    Role findByTitle(String title);

    List<Role> findAll();
}
