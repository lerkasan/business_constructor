package ua.com.brdo.business.constructor.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.com.brdo.business.constructor.entity.User;
import ua.com.brdo.business.constructor.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl (UserService userService){
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails loadedUser;

        User existUser = userService.findByUsername(username);

        return existUser;
    }
}
