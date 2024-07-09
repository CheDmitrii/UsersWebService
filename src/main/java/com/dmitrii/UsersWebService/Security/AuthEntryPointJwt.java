package com.dmitrii.UsersWebService.Security;

import com.dmitrii.UsersWebService.Utils.Exeptions.AuthenticationUserException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


// this bean control authentication exceptions

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Autowired
    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthEntryPointJwt(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, AuthenticationUserException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> data = new HashMap<>();
        data.put("error", "Authentication error");
        data.put("message", "Wrong password or email");

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, data);
        out.flush();

//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not not not not not not");


//        handlerExceptionResolver.resolveException(request, response, null,
//                new AuthenticationUserException(new Error("Wrong email or password from point")));
//        if (request.getServletPath().endsWith("/login")) {
//
//            return;
//        }
//        handlerExceptionResolver.resolveException(request, response, null, authException);
    }
}
