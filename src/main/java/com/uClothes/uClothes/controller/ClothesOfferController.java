package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.dto.ResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.uClothes.uClothes.service.ClothesOfferService;

import java.util.UUID;

@Controller
public class ClothesOfferController {

    private final ClothesOfferService clothesOfferService;

    public ClothesOfferController(ClothesOfferService clothesOfferService) {
        this.clothesOfferService = clothesOfferService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseDTO findAll() {
        return this.clothesOfferService.findAllOffers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseDTO getClothesOffer(@PathVariable UUID id) {
        return clothesOfferService.findOffer(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseDTO addClothesOffer(@RequestBody ClothesOffer clothesOffer) {
        return clothesOfferService.addClothesOffer(clothesOffer);
    }

    @PatchMapping("/delete/{id}")
    @ResponseBody
    public ResponseDTO deleteClothesOffer(@PathVariable UUID id) {
        return clothesOfferService.deleteClothesOffer(id);
    }

}

