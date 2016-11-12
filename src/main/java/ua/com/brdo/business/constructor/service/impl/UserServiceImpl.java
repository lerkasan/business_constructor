package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;
    private RoleRepository roleRepo;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }


    @Transactional
    @Override
    public User create(User user) {
        Objects.requireNonNull(user);
        return userRepo.saveAndFlush(user);
    }

    @Transactional
    @Override
    public User update(User user) {
        Objects.requireNonNull(user);
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
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    @Override
    public User register(UserDto userDto, Role role) {
        User newUser = User.of(userDto);
        setEncodedPassword(newUser, userDto.getPassword());
        addRole(newUser, role);
        return create(newUser);
    }

    @Transactional
    @Override
    public User registerUser(UserDto userDto) {
        User newUser = User.of(userDto);
        setEncodedPassword(newUser, userDto.getPassword());
        Role role = roleRepo.findByTitle("ROLE_USER").orElseThrow(() -> new NotFoundException("Role not found"));
        newUser.setRoles(Collections.singleton(role));
        return create(newUser);
    }

    @Override
    public String encodePassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String setEncodedPassword(User user, String password) {
        String passwordHash = encodePassword(password);
        user.setPasswordHash(passwordHash);
        return passwordHash;
    }

    public boolean addRole(User user, Role role) {
        Set<Role> roles = new HashSet<>(user.getRoles());
        if (roles == null) {
            roles = new HashSet<>();
        }
        boolean isSuccess = roles.add(role);
        user.setRoles(roles);
        return isSuccess;
    }

    public boolean removeRole(User user, Role role) {
        boolean isSuccess = false;
        Set<Role> roles = new HashSet<>(user.getRoles());
        if (roles != null) {
            isSuccess = roles.remove(role);
            user.setRoles(roles);
        }
        return isSuccess;
    }

}
