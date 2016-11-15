package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.entity.Role;
import ua.com.brdo.business.constructor.entity.User;

public interface UserService {

    User create(User user);

    User update(User user);

    void delete(Long id);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    boolean grantRole(User user, Role role);

    boolean revokeRole(User user, Role role);

    User register(User user, Role role);

    User registerUser(User user);

    void encodePassword(User user);

    boolean isEmailAvailable(String email);
}
