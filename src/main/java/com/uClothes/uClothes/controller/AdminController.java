package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.service.ClothesOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
@CrossOrigin(origins = "", allowCredentials = "true")
public class AdminController {

    private final ClothesOfferService clothesOfferService;

    public AdminController(ClothesOfferService clothesOfferService) {
        this.clothesOfferService = clothesOfferService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseOfferDTO addClothesOffer(@RequestParam("name") String name,
                                            @RequestParam("description") String description,
                                            @RequestParam("url") String url,
                                            @RequestParam("price") Double price,
                                            @RequestParam("clothingCategory") String clothingCategory,
                                            @RequestParam("gender") String gender,
                                            @RequestParam("image") MultipartFile image) {
        return clothesOfferService.addClothesOffer(name, description, url, price, clothingCategory, gender, image);
    }

    @PatchMapping("/delete/{id}")
    @ResponseBody
    public ResponseOfferDTO deleteClothesOffer(@PathVariable UUID id) {
        return clothesOfferService.deleteClothesOffer(id);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseOfferDTO updateClothesOffer(@PathVariable UUID id,
                                               @RequestParam("name") String name,
                                               @RequestParam("description") String description,
                                               @RequestParam("url") String url,
                                               @RequestParam("price") Double price,
                                               @RequestParam("clothingCategory") String clothingCategory,
                                               @RequestParam("gender") Gender gender,
                                               @RequestParam(value = "image", required = false) MultipartFile image) {
        ClothesOffer updatedOffer = new ClothesOffer();
        updatedOffer.setName(name);
        updatedOffer.setDescription(description);
        updatedOffer.setUrl(url);
        updatedOffer.setPrice(price);
        updatedOffer.setGender(gender);
        updatedOffer.setClothingCategory(ClothingCategory.valueOf(clothingCategory.toUpperCase()));
        return clothesOfferService.updateClothesOffer(id, updatedOffer, image);
    }
}
