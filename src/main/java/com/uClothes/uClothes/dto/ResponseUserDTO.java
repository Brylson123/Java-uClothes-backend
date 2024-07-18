package com.uClothes.uClothes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uClothes.uClothes.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserDTO {
    private boolean isSuccess;
    private UUID id;
    private User user;
    private String error;
    private String token;

    public ResponseUserDTO(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ResponseUserDTO(boolean isSuccess, User user) {
        this.isSuccess = isSuccess;
        this.user = user;
    }

    public ResponseUserDTO(boolean isSuccess, String token) {
        this.isSuccess = isSuccess;
        this.token = token;
    }

    public ResponseUserDTO(String error) {
        this.isSuccess = false;
        this.error = error;
    }
}