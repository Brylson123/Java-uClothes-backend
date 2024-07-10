package com.uClothes.uClothes.repositories;

import com.uClothes.uClothes.domain.ClothesOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClothesRepository extends JpaRepository<ClothesOffer, UUID> {
}
