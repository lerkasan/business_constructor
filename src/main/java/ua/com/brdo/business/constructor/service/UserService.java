package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;

public interface UserService {

    User create(User user);

    User create(User user, Role role);

    User update(User user);

    void delete(long id);

    User findById(long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    boolean grantRole(User user, Role role);

    boolean revokeRole(User user, Role role);

    boolean isEmailAvailable(String email);

    boolean isUsernameAvailable(String username);
}
