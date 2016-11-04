package ua.com.brdo.business.constructor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.ServiceException;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ua.com.brdo.business.constructor.exception.ExceptionMessages.*;

public final class RoleServiceImpl implements RoleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RoleRepository roleRepo;

    @Transactional
    @Override
    public void create(final Role role) throws ServiceException {
        if (role != null) {
            try {
                roleRepo.saveAndFlush(role);
            } catch (DataAccessException e) {
                logger.error(ROLE_CREATION_FAILURE, e);
                throw new ServiceException(ROLE_CREATION_FAILURE, e);
            }
        } else {
            logger.error(ROLE_NULL);
            throw new ServiceException(ROLE_NULL);
        }
    }

    @Transactional
    @Override
    public void update(final Role role) throws ServiceException {
        if (role != null) {
            try {
                roleRepo.saveAndFlush(role);
            } catch (DataAccessException e) {
                logger.error(ROLE_UPDATE_FAILURE, e);
                throw new ServiceException(ROLE_UPDATE_FAILURE, e);
            }
        } else {
            logger.error(ROLE_NULL);
            throw new ServiceException(ROLE_NULL);
        }
    }

    @Transactional
    @Override
    public void delete(final Integer id) throws ServiceException {
        try {
            roleRepo.delete(id);
        } catch (DataAccessException e) {
            logger.error(ROLE_DELETE_FAILURE, e);
            throw new ServiceException(ROLE_DELETE_FAILURE, e);
        }
    }

    @Override
    public Role findById(final Integer id) throws ServiceException {
        try {
            return roleRepo.findOne(id);
        } catch (DataAccessException e) {
            logger.error(ROLE_FIND_FAILURE, e);
            throw new ServiceException(ROLE_FIND_FAILURE, e);
        }
    }

    @Override
    public Role findByTitle(final String title) throws ServiceException {
        if ((title == null) || ("".equals(title))) {
            logger.error(ROLE_NULL_TITLE);
            throw new ServiceException(ROLE_NULL_TITLE);
        }
        try {
            return roleRepo.findByTitle(title);
        } catch (DataAccessException e) {
            logger.error(ROLE_FIND_FAILURE, e);
            throw new ServiceException(ROLE_FIND_FAILURE, e);
        }
    }

    @Override
    public List<Role> findAll() throws ServiceException {
        try {
            return roleRepo.findAll();
        } catch (DataAccessException e) {
            logger.error(ROLE_FIND_FAILURE, e);
            throw new ServiceException(ROLE_FIND_FAILURE, e);
        }
    }

    public boolean addUser(final User user, final Role role) throws ServiceException {
        boolean isSuccess = true;
        Set<User> users = role.getUsers();

        if (users == null) {
            users = new HashSet<>();
            isSuccess &= users.add(user);
        } else {
            if (!users.contains(user)) {
                isSuccess &= users.add(user);
            } else {
                logger.error(USER_ALREADY_GRANTED_THIS_ROLE);
                throw new ServiceException(USER_ALREADY_GRANTED_THIS_ROLE);
            }
        }
        role.setUsers(users);

        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
            isSuccess &= roles.add(role);
        } else {
            if (!roles.contains(role)) {
                isSuccess &= roles.add(role);
            } else {
                logger.error(USER_ALREADY_GRANTED_THIS_ROLE);
                throw new ServiceException(USER_ALREADY_GRANTED_THIS_ROLE);
            }
        }
        user.setRoles(roles);
        return isSuccess;
    }

    public boolean removeRole(final User user, final Role role) throws ServiceException {
        boolean isSuccess = true;
        Set<User> users = role.getUsers();

        if (users == null) {
            logger.error(USER_NOT_GRANTED_THIS_ROLE);
            throw new ServiceException(USER_NOT_GRANTED_THIS_ROLE);
        } else {
            if (!users.contains(user)) {
                logger.error(USER_NOT_GRANTED_THIS_ROLE);
                throw new ServiceException(USER_NOT_GRANTED_THIS_ROLE);
            } else {
                isSuccess &= users.remove(user);
            }
        }
        role.setUsers(users);

        Set<Role> roles = user.getRoles();
        if (roles == null) {
            logger.error(USER_NOT_GRANTED_THIS_ROLE);
            throw new ServiceException(USER_NOT_GRANTED_THIS_ROLE);
        } else {
            if (!roles.contains(role)) {
                logger.error(USER_NOT_GRANTED_THIS_ROLE);
                throw new ServiceException(USER_NOT_GRANTED_THIS_ROLE);
            } else {
                isSuccess &= roles.remove(role);
            }
        }
        user.setRoles(roles);
        return isSuccess;
    }
}


