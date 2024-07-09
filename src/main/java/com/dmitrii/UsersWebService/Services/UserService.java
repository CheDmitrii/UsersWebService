package com.dmitrii.UsersWebService.Services;

import com.dmitrii.UsersWebService.DAOs.UserDAO;
import com.dmitrii.UsersWebService.Models.User;
import com.dmitrii.UsersWebService.Utils.Jwt.JwtUtils;
import com.dmitrii.UsersWebService.Utils.Errors.Error;
import com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException.AccessNotAuthenticatedUserDataException;
import com.dmitrii.UsersWebService.Utils.Exeptions.EmailTakenException;
import com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException.WrongDataUpdatePasswordException;
import com.dmitrii.UsersWebService.Utils.Exeptions.UserNotFoundException;
import com.dmitrii.UsersWebService.Utils.Validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final UserValidator userValidator;
    private final JwtUtils jwtUtils;
    private final Pattern pattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*");


    @Autowired
    public UserService(UserDAO userDAO, UserValidator userValidator, JwtUtils jwtUtils) {
        this.userDAO = userDAO;
        this.userValidator = userValidator;
        this.jwtUtils = jwtUtils;
    }

    public List<User> getAllUsers() {return userDAO.getAllUsers();}
    public User getUserByID(int id) {return userDAO.getUser(id);}

    public User getUserByEmail(String email) {
        return userDAO.getUser(email);
    }

    public String generateToken(UsernamePasswordAuthenticationToken token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("generate token exception");
        }
        return jwtUtils.generateJwtToken(authentication);
    }

    public boolean existUser(int id) {
//        if (!userDAO.existUser(id)) {throw new UserNotFoundExeption();}
        return userDAO.existUser(id);
    }

    public void createUser(User user, BindingResult result) {
        userValidator.validate(user, result);
        if (userDAO.existUser(user.getEmail())) {throw new EmailTakenException();}
        userDAO.createUser(user);
    }

    public void updateUser(User user, BindingResult result) {
        if (!userDAO.existUser(user.getId())) {throw new UserNotFoundException(null, null, null, user.getId());}
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userDAO.getUser(user.getId()).getEmail().equals(details.getUsername())) {
            throw new AccessNotAuthenticatedUserDataException(new Error("You try to update not your own data"));
        }
        userValidator.validate(user, result);
        userDAO.updateUserData(user);
    }

    public void updateUserPassword(int id, String password) {
        if (!pattern.matcher(password).matches()) {throw new WrongDataUpdatePasswordException(new Error("Invalid password"));}
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!details.getUsername().equals(userDAO.getEmail(id))) {
            throw new AccessNotAuthenticatedUserDataException(new Error("You try to update not your own password"));
        }
        userDAO.updateUserPassword(id, password);
    }

    public void deleteUser(int id) {
        if (!userDAO.existUser(id)) {throw new UserNotFoundException(null, null, null, id);}
        userDAO.deleteUser(id);
    }
}
