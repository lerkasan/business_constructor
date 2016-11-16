package ua.com.brdo.business.constructor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

import ua.com.brdo.business.constructor.utils.UserDetailsServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {


    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Test
    public void getUserAdminTest(){
        UserDetails user = userDetailsService.loadUserByUsername("expert");
        assertTrue(user != null);
    }

    @Test
    public void getUserExpertTest(){
        UserDetails user = userDetailsService.loadUserByUsername("admin");
        assertTrue(user != null);
    }

}
