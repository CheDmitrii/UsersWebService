package com.dmitrii.UsersWebService.Configurations;

//import com.dmitrii.UsersWebService.Repositories.JwtTokenRepository;
import com.dmitrii.UsersWebService.Security.AccessDeniedHandlerImpl;
import com.dmitrii.UsersWebService.Security.Provider.JWTAuthenticationProvider;
import com.dmitrii.UsersWebService.Security.AuthEntryPointJwt;
import com.dmitrii.UsersWebService.Security.Filters.AuthenticationTokenFilter;
import com.dmitrii.UsersWebService.Utils.Jwt.JwtUtils;
import com.dmitrii.UsersWebService.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userService;
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final AuthEntryPointJwt authEntryPointJwt;


    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userService,
                          JwtUtils jwtUtils,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver,
                          AuthEntryPointJwt authEntryPointJwt) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.authEntryPointJwt = authEntryPointJwt;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(authenticationJwtTokenFilter(), AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                    req
                            .requestMatchers(HttpMethod.POST, "/login").permitAll()
                            .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exc ->
                        exc.authenticationEntryPoint(authEntryPointJwt)); //.accessDeniedHandler(accessDeniedHandler()));
        http.authenticationProvider(jwtAuthenticationProvider());

        return http.build();
    }


//    @Bean
//    public AccessDeniedHandlerImpl accessDeniedHandler() {
//        return new AccessDeniedHandlerImpl();
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider();
    }

    @Bean
    public AuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new AuthenticationTokenFilter(userService, jwtUtils, handlerExceptionResolver);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider()));
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }
}
