package com.onlinestore.onlinestore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String login;
    private String password;
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @OneToOne(fetch = EAGER, cascade = CascadeType.MERGE)
    private TokenEntity token;

    public UserEntity(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

}
