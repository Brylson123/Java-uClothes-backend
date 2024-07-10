package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothesOffer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.uClothes.uClothes.service.ClothesOfferService;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@Controller
public class ClothesOfferController {

    private final ClothesOfferService clothesOfferService;

    public ClothesOfferController(ClothesOfferService clothesOfferService) {
        this.clothesOfferService = clothesOfferService;
    }

    @GetMapping("/")
    @ResponseBody
    public List<ClothesOffer> findAll() {
        return this.clothesOfferService.findAllOffers();
    }
}
