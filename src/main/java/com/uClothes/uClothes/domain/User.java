package com.uClothes.uClothes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

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

    private String username, password, currentTokenId;
    private UserRole role;

    public User(String username, String password, UserRole role, String currentTokenId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.currentTokenId = currentTokenId;
    }
}
