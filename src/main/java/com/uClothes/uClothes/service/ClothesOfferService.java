package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.dto.ResponseDTO;
import com.uClothes.uClothes.repositories.ClothesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClothesOfferService {
    private final ClothesRepository clothesRepository;

    public ClothesOfferService(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
    }

    public ResponseDTO findAllOffers() {
        List<ClothesOffer> offers = clothesRepository.findAll();
        return new ResponseDTO(true, offers);
    }

    public ResponseDTO findOffer(UUID id) {
        if (id == null) {
            return new ResponseDTO(false, null, "Invalid ID");
        }
        Optional<ClothesOffer> offer = clothesRepository.findById(id);
        return offer.map(clothesOffer -> new ResponseDTO(true, clothesOffer))
                .orElseGet(() -> new ResponseDTO(false, id, "Offer not found"));
    }

    public ResponseDTO addClothesOffer(ClothesOffer clothesOffer) {
        if (clothesOffer == null || clothesOffer.getName() == null || clothesOffer.getPrice() == null) {
            return new ResponseDTO(false, null, "Invalid offer data");
        }
        clothesOffer.setId(UUID.randomUUID());
        clothesRepository.save(clothesOffer);
        return new ResponseDTO(true, clothesOffer.getId());
    }

    public ResponseDTO deleteClothesOffer(UUID id) {
        Optional<ClothesOffer> clothesOffer = clothesRepository.findById(id);
        if (clothesOffer.isPresent()) {
            clothesRepository.delete(clothesOffer.get());
            return new ResponseDTO(true, id);
        }
        return new ResponseDTO(false, id, "Offer not found");
    }
}
