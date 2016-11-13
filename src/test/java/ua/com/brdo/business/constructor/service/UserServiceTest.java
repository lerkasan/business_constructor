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
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;
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
    private UserDto userDto;
    private Role role;

    @SneakyThrows
    @Before
    public void init() {
        userDto = new UserDto();
        userDto.setUsername("test_user@mail.com");
        userDto.setEmail("test_user@mail.com");
        userDto.setPassword("12345678");

        mockUser = User.of(userDto);
        mockUser.setId(1L);

        when(roleRepo.findByTitle("ROLE_USER")).thenReturn(Optional.of(new Role(1L, "ROLE_USER")));
        when(userRepo.saveAndFlush(any(User.class))).thenReturn(mockUser);

        role = roleRepo.findByTitle("ROLE_USER").get();
    }

    @SneakyThrows
    @Test
    public void registerUserTest() {
        userService.registerUser(userDto);
        verify(userRepo, times(1)).saveAndFlush(User.of(userDto, role));
    }

    @SneakyThrows
    @Test
    public void registerTest() {
        userService.register(userDto, role);
        verify(userRepo, times(1)).saveAndFlush(User.of(userDto, role));
    }

    @SneakyThrows
    @Test
    public void encodePasswordTest() {
        userService.encodePassword(mockUser, "1234567890");
        assertNotEquals("1234567890", mockUser.getPasswordHash());
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
