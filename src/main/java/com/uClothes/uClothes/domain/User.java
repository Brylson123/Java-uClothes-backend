package com.uClothes.uClothes.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String username;

    private String password;

    private String currentTokenId;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    public User(String username, String password, UserRole role, String currentTokenId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.currentTokenId = currentTokenId;
    }
}
