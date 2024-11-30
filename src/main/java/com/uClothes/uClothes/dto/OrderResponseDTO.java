package com.uClothes.uClothes.dto;

import com.uClothes.uClothes.domain.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {
    private boolean success;
    private String message;
    private Order order;

}
