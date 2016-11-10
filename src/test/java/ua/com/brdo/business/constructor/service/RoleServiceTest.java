package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DataJpaTest
public class RoleServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private Role expectedRole;

    @SneakyThrows
    @Before
    public void init() {
        //given
        expectedRole = new Role();
        expectedRole.setTitle("ROLE_TEST");
        User addedUser = new User();
        addedUser.setUsername("user@mail.com");
        addedUser.setEmail("user@mail.com");
        addedUser.setPasswordHash("12345678");
    }

    @SneakyThrows
    @Test
    @Rollback
    public void createRoleTest() {
        //when
        Role actualRole = roleService.create(expectedRole);
        //then
        assertEquals(expectedRole, actualRole);
    }

    @SneakyThrows
    @Test
    @Rollback
    public void updateRoleTest() {
        //when
        Role actualRole = roleService.findByTitle("ROLE_ADMIN");
        actualRole.setTitle("ROLE_ADMINISTRATOR");
        actualRole = roleService.update(actualRole);
        //then
        assertEquals("ROLE_ADMINISTRATOR", actualRole.getTitle());
    }

    @SneakyThrows
    @Test
    @Rollback
    public void deleteRoleTest() {
        //when
        Role actualRole = roleService.findByTitle("ROLE_ADMIN");
        roleService.delete(actualRole.getId());
        actualRole = roleService.findByTitle("ROLE_ADMIN");
        //then
        assertEquals(null, actualRole);
    }

    @SneakyThrows
    @Test
    public void addUserTest() {
        //given
        User expectedUser = userService.findById(3L);
        if (expectedUser.getRoles() != null) {
            assertFalse(expectedUser.getRoles().contains(expectedRole));
        }
        if (expectedRole.getUsers() != null) {
            assertFalse(expectedRole.getUsers().contains(expectedUser));
        }
        //when
        roleService.addUser(expectedUser, expectedRole);
        //then
        assertTrue(expectedUser.getRoles().contains(expectedRole));
        assertTrue(expectedRole.getUsers().contains(expectedUser));
    }

   /* @SneakyThrows
    @Test
    @Rollback
    public void removeUserTest() {
        //given
        User expectedUser = userService.findById(4L);
        expectedRole = roleService.findByTitle("ROLE_ADMIN");
        assertTrue(expectedUser.getRoles().contains(expectedRole));
        assertTrue(expectedRole.getUsers().contains(expectedUser));
        //when
        roleService.removeUser(expectedUser, expectedRole);
        //then
        if (expectedUser.getRoles() != null) {
            assertFalse(expectedUser.getRoles().contains(expectedRole));
        }
        if (expectedRole.getUsers() != null) {
            assertFalse(expectedRole.getUsers().contains(expectedUser));
        }
    } */

    @SneakyThrows
    @Test
    public void findByTitleTest() {
        assertEquals("ROLE_USER", roleService.findByTitle("ROLE_USER").getTitle());
    }

    @SneakyThrows
    @Test
    public void findByIdTest() {
        assertNotNull(userService.findById(1L));
    }
}