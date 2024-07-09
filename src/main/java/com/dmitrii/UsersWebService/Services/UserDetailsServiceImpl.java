package com.dmitrii.UsersWebService.Services;

import com.dmitrii.UsersWebService.DAOs.UserDAO;
import com.dmitrii.UsersWebService.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDAO userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("email: " + email);
        User user = userDAO.getFullUser(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Email %s is not found", email));
        }
//        return new UserDetailImpl(user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true,
                true, true, true, new HashSet<>());
    }
}
