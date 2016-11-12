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

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

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
        User user = userService.findById(1L);
        List<User> users = new ArrayList<>();
        users.add(user);
        expectedRole = new Role();
        expectedRole.setTitle("ROLE_TEST");
    }

    @SneakyThrows
    @Test
    @Rollback
    public void createRoleTest() {
        Role actualRole = roleService.create(expectedRole);

        assertEquals(expectedRole, actualRole);
    }

    @SneakyThrows
    @Test
    @Rollback
    public void updateRoleTest() {
        Role actualRole = roleService.findByTitle("ROLE_ADMIN").get();
        actualRole.setTitle("ROLE_ADMINISTRATOR");
        actualRole = roleService.update(actualRole);

        assertEquals("ROLE_ADMINISTRATOR", actualRole.getTitle());
    }

    @SneakyThrows
    @Test
    @Rollback
    public void deleteRoleTest() {
        Role actualRole = roleService.findByTitle("ROLE_ADMIN").get();
        roleService.delete(actualRole.getId());

        assertFalse(roleService.findByTitle("ROLE_ADMIN").isPresent());
    }

}