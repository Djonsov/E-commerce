package ru.ecommerce.highstylewear.dto;

import lombok.*;
import ru.ecommerce.highstylewear.model.Order;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO extends GenericDTO {
    private String login;
    private String password;
    private String email;
    private LocalDate birthDate;
    private String surName;
    private String name;
    private String patronim;
    private String phone;
    private String address;
    private RoleDTO role;
    private List<Long> orders;
    private Long bucket;
    private String changePasswordToken;
}
