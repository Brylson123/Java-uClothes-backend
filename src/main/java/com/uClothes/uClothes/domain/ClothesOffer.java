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
@Table(name = "offers")
public class ClothesOffer {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    private String description;

    private String imageName;

    private Double price;

    @Enumerated(EnumType.STRING)
    private ClothingCategory clothingCategory;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String size;

    private Boolean active;


    public ClothesOffer(String name, String description, Double price, ClothingCategory clothingCategory, Gender gender, String size, String imageName, boolean active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.clothingCategory = clothingCategory;
        this.gender = gender;
        this.size = size;
        this.imageName = imageName;
        this.active = active;
    }
}
