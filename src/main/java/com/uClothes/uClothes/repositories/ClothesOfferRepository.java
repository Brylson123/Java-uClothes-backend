package com.uClothes.uClothes.repositories;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClothesOfferRepository extends JpaRepository<ClothesOffer, UUID> {
    List<ClothesOffer> findByClothingCategory(ClothingCategory category);
    List<ClothesOffer> findByClothingCategoryAndGender(ClothingCategory category, Gender gender);
    List<ClothesOffer> findByGender(Gender gender);
    List<ClothesOffer> findByActiveTrue();
}
