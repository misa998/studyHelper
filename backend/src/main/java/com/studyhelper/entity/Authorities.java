package com.studyhelper.entity;

import javax.persistence.*;

@Entity
@Table(name="authorities")
public class Authorities {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="username")
    private User username;

    @Column(name="authority")
    private String authority = "ROLE_USER";

    public Authorities(String auth, User user) {
        this.username = user;
        this.authority = auth;
    }

    public Authorities(User username) {
        this.username = username;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Authorities() {
    }

}
