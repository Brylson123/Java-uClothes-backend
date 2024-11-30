package com.uClothes.uClothes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDTO {
    private UUID productId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPhoneNumber;
    private String addressStreet;
    private String addressCity;
    private String addressZipCode;
    private String addressParcelLockerNumber;
    private double totalPrice;
}
