package com.uClothes.uClothes.repositories;

import com.uClothes.uClothes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClothesRepository extends JpaRepository<User, UUID> {
}
