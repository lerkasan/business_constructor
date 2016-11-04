package ua.com.brdo.business.constructor.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.annotation.Unique;
import ua.com.brdo.business.constructor.exception.ServiceException;
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.UserService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ua.com.brdo.business.constructor.exception.ExceptionMessages.*;

public final class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void create(User user) throws ServiceException {
        if (user != null) {
            try {
                userRepo.saveAndFlush(user);
            } catch (DataAccessException e) {
                logger.error(USER_CREATION_FAILURE, e);
                throw new ServiceException(USER_CREATION_FAILURE, e);
            }
        } else {
            logger.error(USER_NULL);
            throw new ServiceException(USER_NULL);
        }
    }

    @Transactional
    @Override
    public void update(User user) throws ServiceException {
        if (user != null) {
            try {
                userRepo.saveAndFlush(user);
            } catch (DataAccessException e) {
                logger.error(USER_UPDATE_FAILURE, e);
                throw new ServiceException(USER_UPDATE_FAILURE, e);
            }
        } else {
            logger.error(USER_UPDATE_FAILURE);
            throw new ServiceException(USER_NULL);
        }
    }

    @Transactional
    @Override
    public void delete(Long id) throws ServiceException {
        try {
            userRepo.delete(id);
        } catch (DataAccessException e) {
            logger.error(USER_DELETE_FAILURE, e);
            throw new ServiceException(USER_DELETE_FAILURE, e);
    }
    }

    @Override
    public User findById(Long id) throws ServiceException {
        try {
            return userRepo.findOne(id);
        } catch (DataAccessException e) {
            logger.error(USER_FIND_FAILURE, e);
            throw new ServiceException(USER_FIND_FAILURE, e);
        }
    }

    @Override
    public User findByUsername(String username) throws ServiceException {
        if ( (username == null) || ("".equals(username)) ) {
            logger.error(USER_NULL_USERNAME);
            throw new ServiceException(USER_NULL_USERNAME);
        }
        try {
            return userRepo.findByUsername(username);
        } catch (DataAccessException e) {
            logger.error(USER_FIND_FAILURE, e);
            throw new ServiceException(USER_FIND_FAILURE, e);
        }
    }

    @Override
    public User findByEmail(String email) throws ServiceException {
        if ( (email == null) || ("".equals(email)) ) {
            logger.error(USER_NULL_EMAIL);
            throw new ServiceException(USER_NULL_EMAIL);
        }
        try {
            return userRepo.findByEmail(email);
        } catch (DataAccessException e) {
            logger.error(USER_FIND_FAILURE, e);
            throw new ServiceException(USER_FIND_FAILURE, e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userRepo.findAll();
        } catch (DataAccessException e) {
            logger.error(USER_FIND_FAILURE, e);
            throw new ServiceException(USER_FIND_FAILURE, e);
        }
    }

    @Override
    public boolean validate(UserDto userDto) throws ServiceException {
        if ( (userDto != null) && (userDto.checkPasswordsMatch()) && ("".equals(checkUniqueFields(userDto))) ) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public User register(UserDto userDto) throws ServiceException {
        User newUser = null;
        if (validate(userDto)) {
            newUser = new User(userDto);
            newUser.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
            create(newUser);
        }
        return newUser;
    }

    public boolean addRole(User user, Role role) throws ServiceException {
        boolean isSuccess = true;
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
        return isSuccess;
    }

    public boolean removeRole(User user, Role role) throws ServiceException {
        boolean isSuccess = true;
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
        return isSuccess;
    }

    @Override
    public String checkUniqueFields(UserDto userDto) throws ServiceException {
        Class<?> cls = userDto.getClass();
        Class<?> serviceCls = this.getClass();
        Field[] fields = cls.getDeclaredFields();
        String errorStr = "";

        for (Field field : fields) {
            if (field.isAnnotationPresent(Unique.class)) {
                try {
                    String fieldNameLowcase = field.getName();
                    String fieldName = fieldNameLowcase.substring(0, 1).toUpperCase() + fieldNameLowcase.substring(1);
                    Method findByMethod = serviceCls.getMethod("findBy" + fieldName, String.class);
                    Method getter = cls.getMethod("get" + fieldName);
                    Object fieldValue = getter.invoke(this);
                    User foundUser = (User) findByMethod.invoke(this, fieldValue);
                    if (foundUser != null) {
                        errorStr += " " + field.getName() + FIELD_NOT_UNIQUE + field.getName() + ". ";
                    }
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    logger.error("Reflection Exception: ", e);
                    throw new ServiceException("Reflection Exception", e);
                }
            }
        }
        return errorStr;
    }

}
