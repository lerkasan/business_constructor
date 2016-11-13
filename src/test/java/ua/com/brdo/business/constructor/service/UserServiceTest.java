package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.entity.Role;
import ua.com.brdo.business.constructor.entity.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.impl.UserServiceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @SneakyThrows
    @Before
    public void init() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test_user@mail.com");
        mockUser.setEmail("test_user@mail.com");
        mockUser.setPassword("12345678");

        when(roleRepo.findByTitle("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(mockUser);

        role = roleRepo.findByTitle("ROLE_USER").get();
    }

    @SneakyThrows
    @Test
    public void registerUserTest() {
        User user = userService.registerUser(mockUser);
        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @SneakyThrows
    @Test
    public void registerTest() {
        User user = userService.register(mockUser, role);
        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @SneakyThrows
    @Test
    public void encodePasswordTest() {
        userService.encodePassword(mockUser);
        assertNotEquals("12345678", mockUser.getPassword());
    }

    @SneakyThrows
    @Test
    public void grantRoleTest() {
        userService.grantRole(mockUser, role);
        assertTrue(mockUser.getRoles().contains(role));
    }

    @SneakyThrows
    @Test
    public void revokeRoleTest() {
        userService.revokeRole(mockUser, role);
        assertFalse(mockUser.getRoles().contains(role));
    }
}
