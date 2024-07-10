package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.repositories.ClothesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothesOfferService {
    private final ClothesRepository clothesRepository;

    public ClothesOfferService(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    public List<ClothesOffer> findAllOffers(){
        return clothesRepository.findAll();
    }
}
