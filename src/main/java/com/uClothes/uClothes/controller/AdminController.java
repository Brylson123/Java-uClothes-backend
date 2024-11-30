package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.service.ClothesOfferService;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@CrossOrigin(origins = {""}, allowCredentials = "true")
public class AdminController {
    private final ClothesOfferService clothesOfferService;

    public AdminController(ClothesOfferService clothesOfferService) {
        this.clothesOfferService = clothesOfferService;
    }

    @PostMapping({"/add"})
    @ResponseBody
    public ResponseOfferDTO addClothesOffer(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("price") Double price, @RequestParam("clothingCategory") String clothingCategory, @RequestParam("gender") String gender, @RequestParam("size") String size,  @RequestParam("active") Boolean active, @RequestParam("image") MultipartFile image) {
        return this.clothesOfferService.addClothesOffer(name, description, price, clothingCategory, gender, size,  active, image);
    }

    @DeleteMapping({"/delete/{id}"})
    @ResponseBody
    public ResponseOfferDTO deleteClothesOffer(@PathVariable UUID id) {
        return this.clothesOfferService.deleteClothesOffer(id);
    }

    @PutMapping({"/update/{id}"})
    @ResponseBody
    public ResponseOfferDTO updateClothesOffer(@PathVariable UUID id, @RequestParam("name") String name, @RequestParam("description") String description,  @RequestParam("price") Double price, @RequestParam("active") Boolean active, @RequestParam("clothingCategory") String clothingCategory, @RequestParam("gender") String gender, @RequestParam("size") String size, @RequestParam(value = "image", required = false) MultipartFile image) {
        ClothesOffer updatedOffer = new ClothesOffer();
        updatedOffer.setName(name);
        updatedOffer.setDescription(description);
        updatedOffer.setActive(active);
        updatedOffer.setPrice(price);
        updatedOffer.setActive(active);
        updatedOffer.setGender(Gender.valueOf(gender.toUpperCase()));
        updatedOffer.setSize(size);
        updatedOffer.setClothingCategory(ClothingCategory.valueOf(clothingCategory.toUpperCase()));
        return this.clothesOfferService.updateClothesOffer(id, updatedOffer, image);
    }
}
