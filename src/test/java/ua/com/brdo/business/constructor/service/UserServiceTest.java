package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
/*
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/schema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/data.sql")
})
*/
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private User expectedUser;

    @SneakyThrows
    @Before
    public void init() {
        //given
        Role role = roleService.findById(1L);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        expectedUser = new User();
        expectedUser.setUsername("user@mail.com");
        expectedUser.setEmail("user@mail.com");
        expectedUser.setPasswordHash("12345678");
        expectedUser.setRoles(roles);
    }

    @SneakyThrows
    @Test
    @Rollback
    public void createUserTest() {
        //when
        User actualUser = userService.create(expectedUser);
        //then
        assertEquals(expectedUser, actualUser);
    }

    @SneakyThrows
    @Test
    @Rollback
    public void updateUserTest() {
        //when
        User actualUser = userService.findByEmail("user1@mail.com");
        actualUser.setFirstName("First");
        actualUser = userService.update(actualUser);
        //then
        assertEquals("First", actualUser.getFirstName());
    }

    @SneakyThrows
    @Test
    @Rollback
    public void deleteUserTest() {
        //when
        User actualUser = userService.findByEmail("user1@mail.com");
        userService.delete(actualUser.getId());
        actualUser = userService.findByEmail(expectedUser.getEmail());
        //then
        assertEquals(null, actualUser);
    }

    @SneakyThrows
    @Test
    @Rollback
    public void registerUserTest() {
        //given
        Role role = roleService.findById(1L);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user@mail.com");
        userDto.setEmail("test_user@mail.com");
        userDto.setPassword("12345678");
        //when
        userService.registerUser(userDto);
        //then
        assertNotEquals(null, userService.findByEmail(userDto.getEmail()));
    }

    @SneakyThrows
    @Test
    @Rollback
    public void registerTest() {
        //given
        Role role = roleService.findById(2L);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        UserDto userDto = new UserDto();
        userDto.setUsername("test_user@mail.com");
        userDto.setEmail("test_user@mail.com");
        userDto.setPassword("12345678");
        //when
        userService.register(userDto, role);
        //then
        assertNotNull(userService.findByEmail(userDto.getEmail()));
        assertTrue(userService.findByEmail(userDto.getEmail()).getRoles().contains(role));
    }

    @SneakyThrows
    @Test
    public void setEncodedPasswordTest() {
        //when
        userService.setEncodedPassword(expectedUser, "1234567890");
        //then
        assertNotEquals("1234567890", expectedUser.getPasswordHash());
    }

    @SneakyThrows
    @Test
    public void addRoleTest() {
        //given
        Role role = roleService.findById(2L);
        //when
        userService.addRole(expectedUser, role);
        //then
        assertTrue(expectedUser.getRoles().contains(role));
        assertTrue(role.getUsers().contains(expectedUser));
    }

    @SneakyThrows
    @Test
    public void removeRoleTest() {
        //given
        Role role = roleService.findById(2L);
        //when
        userService.removeRole(expectedUser, role);
        //then
        assertFalse(expectedUser.getRoles().contains(role));
        assertFalse(role.getUsers().contains(expectedUser));
    }

    @SneakyThrows
    @Test
    public void findByUsernameTest() {
        assertEquals("user1", userService.findByUsername("user1").getUsername());
    }

    @SneakyThrows
    @Test
    public void findByEmailTest() {
        assertEquals("user1@mail.com", userService.findByEmail("user1@mail.com").getEmail());
    }

    @SneakyThrows
    @Test
    public void findByIdTest() {
        assertNotNull(userService.findById(1L));
    }


}
