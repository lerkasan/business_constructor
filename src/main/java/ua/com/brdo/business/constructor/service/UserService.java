package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.exception.ServiceException;
import ua.com.brdo.business.constructor.model.dto.UserDto;
import ua.com.brdo.business.constructor.model.entity.User;

import java.util.List;

public interface UserService {

    void create(User user) throws ServiceException;

    void update(User user) throws ServiceException;

    void delete(Long id) throws ServiceException;

    User findById(Long id) throws ServiceException;

    User findByUsername(String username) throws ServiceException;

    User findByEmail(String email) throws ServiceException;

    List<User> findAll() throws ServiceException;

    User register(UserDto userDto) throws ServiceException;

    boolean validate(UserDto userDto) throws ServiceException;

    String checkUniqueFields(UserDto userDto) throws ServiceException;

}
