package ua.com.brdo.business.constructor.service;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import static org.mockito.Matchers.anyString;
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

    private final String email = "test_user@mail.com";

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
        when(userRepo.countByEmailIgnoreCase(anyString())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();

                if (((String) args[0]).equalsIgnoreCase(email)) return 1;
                else return 0;
            }
        });
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

    @Test
    public void isEmailTestTrue() {
        assertTrue(userService.isEmail(email.toUpperCase()));

    }
    @Test
    public void isEmailTestFalse() {
        assertFalse(userService.isEmail(email+"Incorrect"));
        assertFalse(userService.isEmail(null));
    }
}
