package com.example.it.test.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {

    private Long id;

    @NotBlank
    @Size(min = 4, max = 64)
    private String username;

    @NotBlank
    @Size(min = 4, max = 64)
    private String password;

    @NotBlank
    @Size(min = 4, max = 64)
    private String email;

    public Person(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
