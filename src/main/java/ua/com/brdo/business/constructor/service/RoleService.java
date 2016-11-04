package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.exception.ServiceException;
import ua.com.brdo.business.constructor.model.entity.Role;

import java.util.List;

public interface RoleService {
    void create(Role role) throws ServiceException;

    void update(Role role) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    Role findById(Integer id) throws ServiceException;

    Role findByTitle(String title) throws ServiceException;

    List<Role> findAll() throws ServiceException;
}
