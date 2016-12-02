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
import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.repository.UserRepository;
import ua.com.brdo.business.constructor.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class UserServiceImplTest {

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
        when(userRepo.findByEmail("test_user@mail.com")).thenReturn(Optional.empty());
        when(userRepo.countByEmailIgnoreCase("some_user1@mail.com")).thenReturn(1);

        role = roleRepo.findByTitle("ROLE_USER").get();
    }

    @Test
    public void shouldCreateUserTest() {
        User user = userServiceWithMocks.create(mockUser);

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
    public void shouldReturnFalseForNonUniqueEmailTest() {
        String nonUniqueEmail = "nonUniqueEmail@mail.com";
        when(userRepo.countByEmailIgnoreCase(nonUniqueEmail)).thenReturn(1);
        assertFalse(userServiceWithMocks.isEmailAvailable(nonUniqueEmail));
    }

    @Test
    public void shouldReturnTrueForUniqueTest() {
        String uniqueEmail = "uniqueEmail@mail.com";
        when(userRepo.countByEmailIgnoreCase(uniqueEmail)).thenReturn(0);
        assertTrue(userServiceWithMocks.isEmailAvailable(uniqueEmail));
    }
}
