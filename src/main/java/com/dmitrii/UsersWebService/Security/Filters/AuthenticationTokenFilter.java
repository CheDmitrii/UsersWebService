package com.dmitrii.UsersWebService.Security.Filters;

import com.dmitrii.UsersWebService.Utils.Errors.Error;
import com.dmitrii.UsersWebService.Utils.Exeptions.AuthenticationUserException;
import com.dmitrii.UsersWebService.Utils.Jwt.JwtUtils;
import com.dmitrii.UsersWebService.Services.UserDetailsServiceImpl;
import com.dmitrii.UsersWebService.Utils.Exeptions.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public AuthenticationTokenFilter(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils, HandlerExceptionResolver handlerExceptionResolver) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt == null && !request.getServletPath().endsWith("/login")) {
                handlerExceptionResolver.resolveException(request, response, null, new AuthenticationUserException(new Error("You should be singed in for this url")));
                return;
            }
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        } catch (Exception e) {
            // TODO: resolve exception
            logger.error("Cannot set user authentication: {}", e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Auth");

        if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7);
//        }
        return null;
    }
}
