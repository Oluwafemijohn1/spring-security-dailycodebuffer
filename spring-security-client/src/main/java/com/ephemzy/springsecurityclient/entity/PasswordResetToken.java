package com.ephemzy.springsecurityclient.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String token;
    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private User user;

    private static final int EXPIRATION_TIME = 10;

    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
        this.user = user;
    }

    public PasswordResetToken(String token) {
        this.token = token;
        this.expirationTime = calculateExpirationTime(EXPIRATION_TIME);
    }

//    public static PasswordResetToken findByToken(String token) {
//    }

    private Date calculateExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
