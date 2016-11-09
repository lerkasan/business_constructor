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
import ua.com.brdo.business.constructor.model.entity.Role;
import ua.com.brdo.business.constructor.model.entity.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        User user = userService.findById(1L);
        List<User> users = new ArrayList<>();
        users.add(user);
        expectedRole = new Role();
        expectedRole.setTitle("ROLE_TEST");
        expectedRole.setUsers(users);
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
        User expectedUser = userService.findById(1L);
        //when
        roleService.addUser(expectedUser, expectedRole);
        //then
        assertTrue(expectedUser.getRoles().contains(expectedRole));
        assertTrue(expectedRole.getUsers().contains(expectedUser));
    }

   /* @SneakyThrows
    @Test
    public void removeUserTest() {
        //given
        User expectedUser = userService.findById(1L);
        expectedRole = roleService.findByTitle("ROLE_USER");
        //when
        roleService.removeUser(expectedUser, expectedRole);
        //then
        assertNull(expectedUser.getRoles());
        assertNull(expectedRole.getUsers());
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