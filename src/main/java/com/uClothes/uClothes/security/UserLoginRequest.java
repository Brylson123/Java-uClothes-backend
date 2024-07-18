package com.uClothes.uClothes.security;

import com.uClothes.uClothes.domain.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequest {
    private String username, password;
    UserRole userRole;

    public UserLoginRequest(String username, String password, UserRole userRole) {
        this.username = username;
        this.userRole = userRole;
        this.password = password;
    }
}
