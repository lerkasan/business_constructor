package ua.com.brdo.business.constructor.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    boolean addRole(User user, Role role);

    boolean removeRole(User user, Role role);

    User findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    @Transactional
    User register(UserDto userDto, Role role);

    @Transactional
    User registerUser(UserDto userDto);

    String encodePassword(String password);

    public String setEncodedPassword(User user, String password);

}
