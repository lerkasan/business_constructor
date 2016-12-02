package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.UserService;

@Service("UserService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final String ROLE_USER = "ROLE_USER";

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
    public User update(User user) {
        Objects.requireNonNull(user);
        return userRepo.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepo.delete(id);
    }

    @Override
    public User findById(long id) {
        return userRepo.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Expected username is empty");
        }
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with given username was not found."));
    }

    @Override
    public User findByEmail(String email) {
        if ("".equals(email)) {
            throw new IllegalArgumentException("Expected email is empty");
        }
        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User with given e-mail was not found."));
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    @Override
    public User create(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        if (user.getUsername() == null) {
            user.setUsername(user.getEmail());
        }
        encodePassword(user);
        grantRole(user, role);
        return userRepo.saveAndFlush(user);
    }

    @Transactional
    @Override
    public User create(User user) {
        if (user.getAuthorities().isEmpty()) {
            return create(user, roleRepo.findByTitle(ROLE_USER).orElseThrow(() -> new DataAccessException("Role not found.") {
            }));
        }
        return userRepo.saveAndFlush(user);
    }

    private void encodePassword(User user) {
        Objects.requireNonNull(user);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(user.getRawPassword())));
        for (int index = 0; index < user.getRawPassword().length; index++) {
            user.getRawPassword()[index] = ' ';
        }
    }

    @Override
    public boolean grantRole(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        return user.grantAuthorities(role);
    }

    @Override
    public boolean revokeRole(User user, Role role) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(role);
        return user.revokeAuthorities(role);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        if (email == null) {
            return false;
        }
        return userRepo.countByEmailIgnoreCase(email) == 0;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null) {
            return false;
        }
        return userRepo.countByUsernameIgnoreCase(username) == 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with given user was not found."));
    }
}
