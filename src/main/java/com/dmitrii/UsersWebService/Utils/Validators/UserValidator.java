package com.dmitrii.UsersWebService.Utils.Validators;

import com.dmitrii.UsersWebService.Models.User;
import com.dmitrii.UsersWebService.Utils.Exeptions.UserValidDataException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
//    private final UserDAO userDAO;
//
//    @Autowired
//    public UserValidator(UserDAO userDAO) {
//        this.userDAO = userDAO;
//    }

    @Override
    public boolean supports(Class<?> clazz) {return User.class.equals(clazz);}

    @Override
    public void validate(Object target, Errors errors) {
//        System.out.println(errors.getFieldErrors());
        if (errors.hasErrors()) {
            UserValidDataException exception = new UserValidDataException();
            if (errors.hasFieldErrors("first_name")) {exception.getUserValidDataError().setFirst_name(errors.getFieldErrors("first_name").get(0).getDefaultMessage());}
            if (errors.hasFieldErrors("second_name")) {exception.getUserValidDataError().setLast_name(errors.getFieldErrors("last_name").get(0).getDefaultMessage());}
            if (errors.hasFieldErrors("age")) {exception.getUserValidDataError().setAge(errors.getFieldErrors("age").get(0).getDefaultMessage());}
            if (errors.hasFieldErrors("email")) {exception.getUserValidDataError().setEmail(errors.getFieldErrors("email").stream().map(FieldError::getDefaultMessage).toList().toArray(new String[0]));}
            if (errors.hasFieldErrors("password")) {exception.getUserValidDataError().setPassword(errors.getFieldErrors("password").stream().map(FieldError::getDefaultMessage).toList().toArray(new String[0]));}
            throw exception;
        }
    }

//    public void validateUserData(Errors errors) throws UserValidDataException {
//        if (errors.hasErrors()) {
//            UserValidDataException exception = new UserValidDataException();
//            if (errors.hasFieldErrors("first_name")) {exception.getUserCreatingError().setFirst_name(errors.getFieldErrors("first_name").get(0).getDefaultMessage());}
//            if (errors.hasFieldErrors("second_name")) {exception.getUserCreatingError().setLast_name(errors.getFieldErrors("last_name").get(0).getDefaultMessage());}
//            if (errors.hasFieldErrors("age")) {exception.getUserCreatingError().setAge(errors.getFieldErrors("age").get(0).getDefaultMessage());}
//            if (errors.hasFieldErrors("email")) {exception.getUserCreatingError().setEmail(errors.getFieldErrors("email").get(0).getDefaultMessage());}
//            if (errors.hasFieldErrors("password")) {exception.getUserCreatingError().setPassword(errors.getFieldErrors("password").stream().map(FieldError::getDefaultMessage).toList().toArray(new String[0]));}
//            throw exception;
//        }
//    }
}
