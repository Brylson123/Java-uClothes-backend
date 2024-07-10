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
@Table(name = "offers")
public class ClothesOffer {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name, description, url;
    private Double price;
    private ClothingCategory clothingCategory;

    public ClothesOffer(String name, String description, String url, Double price, ClothingCategory clothingCategory) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.price = price;
        this.clothingCategory = clothingCategory;
    }
}
