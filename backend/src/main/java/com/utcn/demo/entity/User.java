package com.utcn.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "role")
    private int role;

    @Column(name = "date_joined")
    private Date dateJoined;

    @Column(name = "points", columnDefinition = "DOUBLE")
    private double points;


    @Column(name = "is_banned")
    private int isBanned;


}
