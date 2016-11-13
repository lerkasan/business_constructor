package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.entity.Role;
import ua.com.brdo.business.constructor.entity.User;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;
    private RoleRepository roleRepo;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
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
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new NotFoundException("User with given e-mail was not found."));
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User with given e-mail was not found."));
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    @Override
    public User register(User user, Role role) {
        if (user.getUsername() == null) {
            user.setUsername(user.getEmail());
        }
        encodePassword(user);
        grantRole(user, role);
        return create(user);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        return register(user, roleRepo.findByTitle("ROLE_USER").orElseThrow(() -> new NotFoundException("Role not found.")));
    }

    @Override
    public void encodePassword(User user) {
        Objects.requireNonNull(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public boolean grantRole(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        return user.grantRole(role);
    }

    @Override
    public boolean revokeRole(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        return user.revokeRole(role);
    }
}
