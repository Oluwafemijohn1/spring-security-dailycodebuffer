package com.ephemzy.springsecurityclient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private String firstname;
    private String lastname;
    private String email;

    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = false;

}
