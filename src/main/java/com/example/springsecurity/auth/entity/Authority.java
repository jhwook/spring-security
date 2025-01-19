package com.example.springsecurity.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authorityName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Authority(String role, User user) {
        this.authorityName = role;
        this.user = user;
    }
}
