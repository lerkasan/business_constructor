package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

public interface UserService {

    User create(User user);

    User update(User user);

    void delete(Long id);

    boolean grantRole(User user, Role role);

    boolean revokeRole(User user, Role role);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    User register(UserDto userDto, Role role);

    User registerUser(UserDto userDto);

    public void encodePassword(User user, String password);
}
