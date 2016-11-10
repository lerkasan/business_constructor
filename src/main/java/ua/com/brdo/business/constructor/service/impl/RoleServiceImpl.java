package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ua.com.brdo.business.constructor.exception.ServiceException;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.service.RoleService;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    @Transactional
    @Override
    public Role create(final Role role) {
        if (role == null) {
            throw new ServiceException(messageSource.getMessage("role.not.found", null, locale));
        }
        return roleRepo.saveAndFlush(role);
    }

    @Transactional
    @Override
    public Role update(final Role role) {
        if (role == null) {
            throw new ServiceException(messageSource.getMessage("role.validation.null", null, locale));
        }
        return roleRepo.saveAndFlush(role);
    }

    @Transactional
    @Override
    public void delete(final Long id) {
        roleRepo.delete(id);
    }

    @Override
    public Role findById(final Long id) {
        return roleRepo.findOne(id);
    }

    @Override
    public Role findByTitle(final String title) {
        return roleRepo.findByTitle(title).orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    public boolean addUser(User user, Role role) {
        boolean isSuccess = false;
        List<User> users = role.getUsers();
        List<Role> roles = user.getRoles();
        if (users == null) {
            users = new ArrayList<>();
        }
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if ((!roles.contains(role)) && (!users.contains(user))) {
            isSuccess = users.add(user) & roles.add(role);
            role.setUsers(users);
            user.setRoles(roles);
        }
        return isSuccess;
    }

    public boolean removeUser(User user, Role role) {
        boolean isSuccess = false;
        List<User> users = role.getUsers();
        List<Role> roles = user.getRoles();
        if ((roles != null) && (users != null) && (roles.contains(role)) && (users.contains(user))) {
            isSuccess = roles.remove(role) & users.remove(user);
            user.setRoles(roles);
            role.setUsers(users);
        }
        return isSuccess;
    }
}


