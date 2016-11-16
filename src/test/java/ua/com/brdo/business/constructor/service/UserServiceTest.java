package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.brdo.business.constructor.entity.Role;
import ua.com.brdo.business.constructor.entity.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    private UserService userService = new UserServiceImpl(userRepo, roleRepo, new BCryptPasswordEncoder());

    private User mockUser;
    private Role role;

    @Before
    public void init() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test_user@mail.com");
        mockUser.setEmail("test_user@mail.com");
        mockUser.setPassword("12345678");

        when(roleRepo.findByTitle("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(mockUser);
        when(userRepo.findByEmail("some_user1@mail.com")).thenReturn(Optional.of(mockUser));
        when(userRepo.findByEmail("test_user@mail.com")).thenReturn(Optional.empty());
        role = roleRepo.findByTitle("ROLE_USER").get();
    }

    @Test
    public void createUserTest() {
        User user = userService.create(mockUser);
        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @Test
    public void createTest() {
        User user = userService.create(mockUser, role);
        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @Test
    public void encodePasswordTest() {
        userService.encodePassword(mockUser);
        assertNotEquals("12345678", mockUser.getPasswordHash());
        assertNotNull(mockUser.getPasswordHash());
    }

    @Test
    public void grantRoleTest() {
        userService.grantRole(mockUser, role);
        assertTrue(mockUser.getRoles().contains(role));
    }

    @Test
    public void revokeRoleTest() {
        userService.revokeRole(mockUser, role);
        assertFalse(mockUser.getRoles().contains(role));
    }

    @Test
    public void isEmailAvailableTest() {
        assertFalse(userService.isEmailAvailable("some_user1@mail.com"));
    }
}
