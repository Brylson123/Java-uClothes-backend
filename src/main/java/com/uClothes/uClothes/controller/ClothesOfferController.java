package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.service.ClothesOfferService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
public class ClothesOfferController {

    private final ClothesOfferService clothesOfferService;

    public ClothesOfferController(ClothesOfferService clothesOfferService) {
        this.clothesOfferService = clothesOfferService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseOfferDTO findAll() {
        return this.clothesOfferService.findAllOffers();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseOfferDTO getClothesOffer(@PathVariable UUID id) {
        return clothesOfferService.findOffer(id);
    }

    @GetMapping("/category/{category}")
    @ResponseBody
    public ResponseOfferDTO findOffersByCategoryAndGender(
            @PathVariable String category,
            @RequestParam(required = false) String gender) {

        ClothingCategory clothingCategory = null;
        if (!category.equalsIgnoreCase("all")) {
            clothingCategory = ClothingCategory.fromString(category);
            if (clothingCategory == null) {
                return new ResponseOfferDTO(false, null, "Invalid category");
            }
        }

        Gender genderEnum = null;
        if (gender != null && !gender.equalsIgnoreCase("all")) {
            try {
                genderEnum = Gender.valueOf(gender.toUpperCase());
            } catch (IllegalArgumentException e) {
                return new ResponseOfferDTO(false, null, "Invalid gender");
            }
        }

        return clothesOfferService.findOffersByCategoryAndGender(clothingCategory, genderEnum);
    }


    @PostMapping("/image/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable UUID id, @RequestParam("image") MultipartFile image) {
        try {
            clothesOfferService.saveImage(id, image);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @GetMapping("/image/{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        return clothesOfferService.getImage(imageName);
    }
}

