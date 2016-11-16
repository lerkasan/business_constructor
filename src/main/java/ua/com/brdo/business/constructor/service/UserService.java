package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.entity.User;

public interface UserService {
    User findByUsername(String username);
}
