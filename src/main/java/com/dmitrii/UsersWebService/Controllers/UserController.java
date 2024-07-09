package com.dmitrii.UsersWebService.Controllers;

import com.dmitrii.UsersWebService.Models.User;
import com.dmitrii.UsersWebService.Services.UserService;
import com.dmitrii.UsersWebService.Utils.Errors.Error;
import com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException.AccessNotAuthenticatedUserDataException;
import com.dmitrii.UsersWebService.Utils.Exeptions.EmailTakenException;
import com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException.UpdateException;
import com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException.WrongDataUpdatePasswordException;
import com.dmitrii.UsersWebService.Utils.Exeptions.UserValidDataException;
import com.dmitrii.UsersWebService.Utils.Messages.JwtResponse;
import com.dmitrii.UsersWebService.Utils.Messages.SuccessMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: update user with empty fields in response

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/login")
    public @ResponseBody JwtResponse login(Authentication authentication, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
//        response.setHeader("Tokeeeen", userService.generateToken(token));
//        return new ResponseEntity<>("success", HttpStatus.OK);
        return new JwtResponse(userService.generateToken(token));
    }

    // in my case don't need
//    @GetMapping("/logout")
//    public @ResponseBody String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
//        return "Success logout";
//    }

    @GetMapping("/users")
    public @ResponseBody List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<SuccessMessage> createUser(@RequestBody @Valid User user, BindingResult result) { // return error message if bad data
        try {
            userService.createUser(user, result);
        } catch (UserValidDataException exception) {
            exception.setData("Data isn't valid", "/users");
            throw exception;
        } catch (EmailTakenException exception) {
            exception.setData("Email already taken", user.getEmail(), "/users");
            throw exception;
        }
        return new ResponseEntity<>(new SuccessMessage("User are created", "/user/create"), HttpStatus.CREATED);
    }


    @GetMapping("/users/{id}")
    public @ResponseBody User getUserByID(@PathVariable("id") int id) {
        return userService.getUserByID(id);
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<SuccessMessage> updateUser(@RequestBody @Valid User user, BindingResult result, @PathVariable("id") int id) {
        user.setId(id);
        try {
            userService.updateUser(user, result);
        } catch (UserValidDataException exception) {
            exception.getUserValidDataError().setError("New data isn't valid");
            exception.getUserValidDataError().setUrl("/users/" + id);
            throw exception;
        } catch (AccessNotAuthenticatedUserDataException e) {
            throw new UpdateException(new Error(e.getError().getError(), "/users/" + id));
        }
        return new ResponseEntity<>(new SuccessMessage("User are updated", "/users/" + id), HttpStatus.OK);
    }

    @PutMapping("/users/{id}/password")
    public ResponseEntity<SuccessMessage> updateUserPassword(@RequestBody User user, @PathVariable("id") int id) {
        try {
            userService.updateUserPassword(id, user.getPassword());
        } catch (WrongDataUpdatePasswordException | AccessNotAuthenticatedUserDataException e) {
            e.getError().setUrl("/users/" + id + "/password");
            throw e;
        }
        return new ResponseEntity<>(new SuccessMessage("Password is updated", "/users/" + id + "/password"), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // doesn't work
    @GetMapping("/access-denied")
    public @ResponseBody HttpEntity<?> accessDenied() {
        return new ResponseEntity<>(new Error("You aren't authorise for this resources"),HttpStatus.FOUND);
    }
}