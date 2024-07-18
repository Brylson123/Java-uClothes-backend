package com.uClothes.uClothes.domain;

public enum ClothingCategory {
    SHIRTS,
    PANTS,
    JACKETS,
    SHOES,
    ACCESSORIES;

    public static ClothingCategory fromString(String category) {
        try {
            return ClothingCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
