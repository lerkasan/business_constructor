package ua.com.brdo.business.constructor.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

public interface UserService {

    @Transactional
    User create(User user);

    @Transactional
    User update(User user);

    @Transactional
    void delete(Long id);

    boolean grantRole(User user, Role role);

    boolean revokeRole(User user, Role role);

    User findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    @Transactional
    User register(UserDto userDto, Role role);

    @Transactional
    User registerUser(UserDto userDto);

    public String encodePassword(User user, String password);

}
