package ru.ecommerce.highstylewear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "uniqueEmail", columnNames = "email"),
                                            @UniqueConstraint(name = "uniqueLogin", columnNames = "login")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "users_seq", allocationSize = 1)
public class User extends GenericModel {

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "surname", nullable = false)
    private String surName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronim")
    private String patronim;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "change_password_token")
    private String changePasswordToken;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "FK_USERS_ROLES"))
    private Role role;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders;

    @OneToOne
    private Bucket bucket;

}
