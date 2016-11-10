package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.exception.ServiceException;
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

    @Transactional
    @Override
    public User create(User user) {
        if (user == null) {
            throw new ServiceException(messageSource.getMessage("user.validation.null", null, locale));
        }
        return userRepo.saveAndFlush(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        if (user == null) {
            throw new ServiceException(messageSource.getMessage("user.validation.null", null, locale));
        }
        return userRepo.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepo.delete(id);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    @Override
    public User register(UserDto userDto, Role role) {
        User newUser = new User(userDto);
        setEncodedPassword(newUser, userDto.getPassword());
        addRole(newUser, role);
        return create(newUser);
    }

    @Transactional
    @Override
    public User registerUser(UserDto userDto) {
        User newUser = new User(userDto);
        setEncodedPassword(newUser, userDto.getPassword());
        Role role = roleRepo.findByTitle("ROLE_USER").orElseThrow(() -> new NotFoundException(messageSource.getMessage("role.not.found", null, locale)));
        addRole(newUser, role);
        return create(newUser);
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public String setEncodedPassword(User user, String password) {
        String passwordHash = encodePassword(password);
        user.setPasswordHash(passwordHash);
        return passwordHash;
    }

    public boolean addRole(User user, Role role) {
        boolean isSuccess = false;
        List<Role> roles = user.getRoles();
        List<User> users = role.getUsers();
        if (roles == null) {
            roles = new ArrayList<>();
        }
        if (users == null) {
            users = new ArrayList<>();
        }
        if ((!roles.contains(role)) && (!users.contains(user))) {
            isSuccess = roles.add(role) & users.add(user);
            user.setRoles(roles);
            role.setUsers(users);
        }
        return isSuccess;
    }

    public boolean removeRole(User user, Role role) {
        boolean isSuccess = false;
        List<Role> roles = user.getRoles();
        List<User> users = role.getUsers();
        if ((roles != null) && (users != null) && (roles.contains(role)) && (users.contains(user))) {
            isSuccess = roles.remove(role) & users.remove(user);
            user.setRoles(roles);
            role.setUsers(users);
        }
        return isSuccess;
    }

}
