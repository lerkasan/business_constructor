package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    List<User> findAll();

    boolean isEmailAvailable(String email);

    boolean isUsernameAvailable(String username);
}
