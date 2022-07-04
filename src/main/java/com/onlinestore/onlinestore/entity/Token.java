package com.onlinestore.onlinestore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String token;

    @OneToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private Date expirationDate;
}
