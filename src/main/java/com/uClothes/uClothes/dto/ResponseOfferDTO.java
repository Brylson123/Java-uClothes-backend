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
public class ResponseOfferDTO {
    private boolean isSuccess;
    private UUID id;
    private String error;
    private ClothesOffer offer;
    private List<ClothesOffer> offers;

    public ResponseOfferDTO(boolean isSuccess, UUID id) {
        this.isSuccess = isSuccess;
        this.id = id;
    }

    public ResponseOfferDTO(boolean isSuccess, ClothesOffer offer) {
        this.isSuccess = isSuccess;
        this.offer = offer;
    }

    public ResponseOfferDTO(boolean isSuccess, List<ClothesOffer> offers) {
        this.isSuccess = isSuccess;
        this.offers = offers;
    }

    public ResponseOfferDTO(boolean isSuccess, UUID id, String error) {
        this.isSuccess = isSuccess;
        this.id = id;
        this.error = error;
    }

    public ResponseOfferDTO(String error) {
        this.isSuccess = false;
        this.error = error;
    }
}
