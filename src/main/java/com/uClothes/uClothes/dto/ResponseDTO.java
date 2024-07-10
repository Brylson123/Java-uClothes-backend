package com.uClothes.uClothes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uClothes.uClothes.domain.ClothesOffer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private boolean isSuccess;
    private UUID id;
    private String error;
    private ClothesOffer offer;
    private List<ClothesOffer> offers;

    public ResponseDTO(boolean isSuccess, UUID id) {
        this.isSuccess = isSuccess;
        this.id = id;
    }

    public ResponseDTO(boolean isSuccess, ClothesOffer offer) {
        this.isSuccess = isSuccess;
        this.offer = offer;
    }

    public ResponseDTO(boolean isSuccess, List<ClothesOffer> offers) {
        this.isSuccess = isSuccess;
        this.offers = offers;
    }

    public ResponseDTO(boolean isSuccess, UUID id, String error) {
        this.isSuccess = isSuccess;
        this.id = id;
        this.error = error;
    }
}
