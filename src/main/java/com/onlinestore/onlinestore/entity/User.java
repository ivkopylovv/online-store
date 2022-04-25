package com.onlinestore.onlinestore.entity;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String username;
    private String login;
    private String password;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    public User(String username, String login, String password) {
        this.username = username;
        this.login = login;
        this.password = password;
    }

}
