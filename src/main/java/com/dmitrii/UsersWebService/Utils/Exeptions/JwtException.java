package com.dmitrii.UsersWebService.Utils.Exeptions;

import com.dmitrii.UsersWebService.Utils.Errors.JwtError;
import lombok.Data;

@Data
public class JwtException extends RuntimeException{
    private JwtError jwtError;


    public JwtException(JwtError jwtError) {
        this.jwtError = jwtError;
    }
}
