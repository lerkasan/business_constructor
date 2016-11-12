package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest

public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private User expectedUser;

    @SneakyThrows
    @Before
    public void init() {

        Role role = roleService.findById(1L);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        expectedUser = new User();
        expectedUser.setUsername("user@mail.com");
        expectedUser.setEmail("user@mail.com");
        expectedUser.setPasswordHash("12345678");
    }

    @SneakyThrows
    @Test
    @Rollback
    public void createUserTest() {

        User actualUser = userService.create(expectedUser);

        assertEquals(expectedUser, actualUser);
    }

    @SneakyThrows
    @Test
    @Rollback
    public void updateUserTest() {

        User actualUser = userService.findByEmail("user1@mail.com").get();
        actualUser.setFirstName("First");
        actualUser = userService.update(actualUser);

        assertEquals("First", actualUser.getFirstName());
    }

    @SneakyThrows
    @Test
    @Rollback
    public void deleteUserTest() {

        User actualUser = userService.findByEmail("user1@mail.com").get();
        userService.delete(actualUser.getId());

        assertFalse(userService.findByEmail(expectedUser.getEmail()).isPresent());
    }

    @SneakyThrows
    @Test
    @Rollback
    public void registerUserTest() {

        Role role = roleService.findById(1L);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user@mail.com");
        userDto.setEmail("test_user@mail.com");
        userDto.setPassword("12345678");

        userService.registerUser(userDto);

        assertTrue(userService.findByEmail(userDto.getEmail()).isPresent());
    }

    @SneakyThrows
    @Test
    @Rollback
    public void registerTest() {

        Role role = roleService.findById(2L);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user@mail.com");
        userDto.setEmail("test_user@mail.com");
        userDto.setPassword("12345678");

        userService.register(userDto, role);

        assertTrue(userService.findByEmail(userDto.getEmail()).isPresent());
        assertTrue(userService.findByEmail(userDto.getEmail()).get().getRoles().contains(role));
    }

    @SneakyThrows
    @Test
    public void setEncodedPasswordTest() {

        userService.setEncodedPassword(expectedUser, "1234567890");

        assertNotEquals("1234567890", expectedUser.getPasswordHash());
    }

    @SneakyThrows
    @Test
    public void addRoleTest() {

        Role role = roleService.findById(2L);

        userService.addRole(expectedUser, role);

        assertTrue(expectedUser.getRoles().contains(role));
    }

    @SneakyThrows
    @Test
    public void removeRoleTest() {

        Role role = roleService.findById(2L);

        userService.removeRole(expectedUser, role);

        assertFalse(expectedUser.getRoles().contains(role));
    }
}
