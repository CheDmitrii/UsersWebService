package com.dmitrii.UsersWebService.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
public class User {
    private int id;

    @NotEmpty(message = "Name shouldn't be empty")
    private String first_name;

    @NotEmpty(message = "Surname shouldn't be empty")
    private String last_name;

    @Min(value = 14, message = "Age should be greater or equals 14")
    private int age;

    @Email(message = "It's not valid email")
    @NotEmpty(message = "Mail shouldn't be empty")
    private String email;

    @NotEmpty(message = "Password shouldn't be empty")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*",
            message = "Password should contain at least one digit, one lower and upper case letters")
    @Size(min = 8, message = "Length of password should be at least 8")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public User() {
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void fillEmptyFields(User user) {
        if (this.first_name == null) {this.first_name = user.getFirst_name();}
        if (this.last_name == null) {this.last_name = user.getLast_name();}
        if (this.email == null) {this.email = user.getEmail();}
    }
}
