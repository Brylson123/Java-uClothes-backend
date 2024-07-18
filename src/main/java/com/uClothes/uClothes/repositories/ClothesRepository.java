package com.uClothes.uClothes.repositories;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClothesRepository extends JpaRepository<ClothesOffer, UUID> {
    List<ClothesOffer> findByClothingCategory(ClothingCategory category);
}
