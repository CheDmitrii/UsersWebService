package com.dmitrii.UsersWebService.Controllers;

import com.dmitrii.UsersWebService.Utils.Exeptions.AuthenticationUserException;
import com.dmitrii.UsersWebService.Utils.Errors.*;
import com.dmitrii.UsersWebService.Utils.Errors.Error;
import com.dmitrii.UsersWebService.Utils.Exeptions.*;
import com.dmitrii.UsersWebService.Utils.Exeptions.UpdateException.UpdateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IOException.class, AuthenticationException.class, SessionAuthenticationException.class, AuthenticationServiceException.class})
    public GlobalExceptionHandler.ErrorInfo handleAuthenticationException(RuntimeException ex, HttpServletRequest request, HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return new GlobalExceptionHandler.ErrorInfo(UrlUtils.buildFullRequestUrl(request), ex.getMessage());
    }

    @ExceptionHandler({JwtException.class})
    public @ResponseBody ResponseEntity<JwtError> jwtExceptionHandler(HttpServletRequest request, JwtException exception) {
        return new ResponseEntity<>(exception.getJwtError(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({EmailTakenException.class})
    public @ResponseBody EmailTakenError emailTakenError(EmailTakenException exception) {
        return exception.getError();
    }

    @ExceptionHandler({UserNotFoundException.class})
    public UserNotFoundError notFoundUser(UserNotFoundException exception) {
        return exception.getError();
    }

    @ExceptionHandler({UserValidDataException.class})
    public @ResponseBody UserValidDataError notValidDataOfUser(UserValidDataException exception) {
        return exception.getUserValidDataError();
    }

    @ExceptionHandler({UpdateException.class})
    public @ResponseBody ResponseEntity<Error> updateException(UpdateException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AuthenticationUserException.class})
    public @ResponseBody ResponseEntity<Error> updateException(AuthenticationUserException exception) {
        return new ResponseEntity<>(exception.getError(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({TestException.class})
    public @ResponseBody ResponseEntity<String> testExceptionHandler(TestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }



    @Getter
    public class ErrorInfo {
        private final String url;
        private final String info;

        ErrorInfo(String url, String info) {
            this.url = url;
            this.info = info;
        }
    }
}
