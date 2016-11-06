package ua.com.brdo.business.constructor.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.brdo.business.constructor.entity.Role;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class) @SpringBootTest
public class UsersRepositoryTest {

    @Autowired
    UsersRepository userRepo;

    @Test
    public void readUsers() throws Exception {
        assertEquals("user", userRepo.findOne(1L).getUsername());
    }

    @Test
    public void readPass() throws Exception {
        assertEquals("password", userRepo.findOne(1l).getPassword());
    }

    @Test
    public void readRole() throws Exception {
        Set<Role> set = userRepo.findOne(1L).getRoles();
        Iterator<Role> iter = set.iterator();
        iter.hasNext();
        Role role = iter.next();
        assertEquals("ADMIN", role.getRole());
    }

}
