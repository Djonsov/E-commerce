package ru.ecommerce.highstylewear.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Setter
public class RegisterDTO {
    private String login;
    private String password;
    private String name;
    private String patronim;
    private String surName;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private String email;
}
