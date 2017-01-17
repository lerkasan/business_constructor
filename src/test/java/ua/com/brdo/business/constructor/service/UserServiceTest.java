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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.UserService;
import ua.com.brdo.business.constructor.service.impl.UserServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserServiceTest {

    private User mockUser;

    private Role role;

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceWithMocks = new UserServiceImpl(userRepo, roleRepo, new BCryptPasswordEncoder());

    private User createUser() {
        User user = new User();
        user.setUsername("UserFromUserService");
        user.setEmail("UserFrom@UserService.com");
        user.setPassword("password");
        user.setRawPassword("password".toCharArray());
        userRepository.saveAndFlush(user);
        return user;
    }

    @Before
    public void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("test_user@mail.com");
        mockUser.setEmail("test_user@mail.com");
        mockUser.setRawPassword("12345678".toCharArray());

        when(roleRepo.findByTitle("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(mockUser);
        when(userRepo.findByEmail("some_user1@mail.com")).thenReturn(Optional.of(mockUser));
        when(userRepo.findByEmail("test_user@mail.com")).thenReturn(Optional.empty());
        when(userRepo.emailAvailable("some_user1@mail.com")).thenReturn(false);

        role = roleRepo.findByTitle("ROLE_USER").get();
    }

    @Test
    public void shouldCreateUserTest() {
        User user = userServiceWithMocks.create(mockUser);

        assertEquals(new String(user.getRawPassword()), "        ");
        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @Test
    public void shouldCreateTest() {
        User user = userServiceWithMocks.create(mockUser, role);

        verify(userRepo, times(1)).saveAndFlush(user);
    }

    @Test
    public void shouldGrantRoleTest() {
        userServiceWithMocks.grantRole(mockUser, role);
        Set<Role> actualRoles = mockUser.getAuthorities();

        assertTrue(actualRoles.contains(role));
    }

    @Test
    public void shouldRevokeRoleTest() {
        userServiceWithMocks.revokeRole(mockUser, role);
        Set<Role> actualRoles = mockUser.getAuthorities();

        assertFalse(actualRoles.contains(role));
    }

    @Test
    public void shouldCheckEmailAvailablityTest() {
        assertFalse(userServiceWithMocks.isEmailAvailable("some_user1@mail.com"));
    }

    @Test
    public void shouldReturnAdminTest() throws UsernameNotFoundException {
        UserDetails user = userService.findByUsername("admin");

        assertNotNull(user);
    }

    @Test
    public void shouldReturnExpertTest() {
        UserDetails user = userService.findByUsername("expert");

        assertNotNull(user);
    }

    @Test
    public void shouldReturnFalseForNonUniqueEmailIest() {
        User user = createUser();
        assertFalse(userService.isEmailAvailable(user.getEmail().toUpperCase()));

    }

    @Test
    public void shouldReturnTrueForUniqueTest() {
        assertTrue(userService.isEmailAvailable("noSuch@email.com"));
    }
}
