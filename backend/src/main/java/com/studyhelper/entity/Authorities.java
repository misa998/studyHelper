package com.studyhelper.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="authorities")
@Getter
@Setter
public class Authorities {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private int id;

    private String authority;

    public Authorities() {
    }

    public Authorities(String name) {
        this.authority = name;
    }
}
