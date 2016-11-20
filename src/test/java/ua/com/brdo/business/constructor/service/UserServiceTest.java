package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.Set;

import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.impl.UserServiceImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @Autowired
    private UserServiceImpl userServiceImpl;

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
        mockUser.setRawPassword("12345678");

        when(roleRepo.findByTitle("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(mockUser);
        when(userRepo.findByEmail("some_user1@mail.com")).thenReturn(Optional.of(mockUser));
        when(userRepo.countByEmailIgnoreCase("some_user1@mail.com")).thenReturn(1);
        when(userRepo.findByEmail("test_user@mail.com")).thenReturn(Optional.empty());
        role = roleRepo.findByTitle("ROLE_USER").get();
    }

    @Test
    public void shouldCreateUserTest() {
        User user = userService.create(mockUser);

        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @Test
    public void shouldCreateTest() {
        User user = userService.create(mockUser, role);

        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @Test
    public void shouldGrantRoleTest() {
        userService.grantRole(mockUser, role);
        Set<Role> actualRoles = mockUser.getAuthorities();

        assertTrue(actualRoles.contains(role));
    }

    @Test
    public void shouldRevokeRoleTest() {
        userService.revokeRole(mockUser, role);
        Set<Role> actualRoles = mockUser.getAuthorities();

        assertFalse(actualRoles.contains(role));
    }

    @Test
    public void shouldCheckEmailAvailablityTest() {
        assertFalse(userService.isEmailAvailable("some_user1@mail.com"));
    }

    @Test
    public void shouldReturnAdminTest() throws UsernameNotFoundException {
        UserDetails user = userServiceImpl.findByUsername("admin");

        assertNotNull(user);
    }

    @Test
    public void shouldReturnExpertTest() {
        UserDetails user = userServiceImpl.findByUsername("expert");

        assertNotNull(user);
    }
}
