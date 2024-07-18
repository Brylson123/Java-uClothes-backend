package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.uClothes.uClothes.service.ClothesOfferService;
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
    public ResponseOfferDTO findOffersByCategory(@PathVariable String category) {
        ClothingCategory clothingCategory = ClothingCategory.fromString(category);
        if (clothingCategory == null) {
            return new ResponseOfferDTO( "Invalid category");
        }
        return clothesOfferService.findOffersByCategory(clothingCategory);
    }

    @PostMapping("/{id}/image")
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

