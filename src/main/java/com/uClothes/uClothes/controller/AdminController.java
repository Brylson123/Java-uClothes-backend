package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.service.ClothesOfferService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseOfferDTO> addClothesOffer(@RequestParam("name") String name,
                                                            @RequestParam("description") String description,
                                                            @RequestParam("url") String url,
                                                            @RequestParam("price") Double price,
                                                            @RequestParam("clothingCategory") String clothingCategory,
                                                            @RequestParam("image") MultipartFile image) {
        return clothesOfferService.addClothesOffer(name, description, url, price, clothingCategory, image);
    }

    @PatchMapping("/delete/{id}")
    @ResponseBody
    public ResponseOfferDTO deleteClothesOffer(@PathVariable UUID id) {
        return clothesOfferService.deleteClothesOffer(id);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<ResponseOfferDTO> updateClothesOffer(@PathVariable UUID id,
                                                               @RequestParam("name") String name,
                                                               @RequestParam("description") String description,
                                                               @RequestParam("url") String url,
                                                               @RequestParam("price") Double price,
                                                               @RequestParam("clothingCategory") String clothingCategory,
                                                               @RequestParam(value = "image", required = false) MultipartFile image) {
        ClothesOffer updatedOffer = new ClothesOffer();
        updatedOffer.setName(name);
        updatedOffer.setDescription(description);
        updatedOffer.setUrl(url);
        updatedOffer.setPrice(price);
        updatedOffer.setClothingCategory(ClothingCategory.valueOf(clothingCategory.toUpperCase()));
        return ResponseEntity.ok(clothesOfferService.updateClothesOffer(id, updatedOffer, image));
    }
}
