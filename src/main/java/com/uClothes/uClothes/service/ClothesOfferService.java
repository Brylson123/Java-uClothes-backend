package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.repositories.ClothesRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ClothesOfferService {
    private final ClothesRepository clothesRepository;
    private final String uploadDir = "uploads/";
    private final Validator validator;

    public ClothesOfferService(ClothesRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public ResponseOfferDTO findAllOffers() {
        List<ClothesOffer> offers = clothesRepository.findAll();
        return new ResponseOfferDTO(true, offers);
    }

    public ResponseOfferDTO findOffer(UUID id) {
        if (id == null) {
            return new ResponseOfferDTO(false, null, "Invalid ID");
        }
        Optional<ClothesOffer> offer = clothesRepository.findById(id);
        return offer.map(clothesOffer -> new ResponseOfferDTO(true, clothesOffer))
                .orElseGet(() -> new ResponseOfferDTO(false, id, "Offer not found"));
    }

    public ResponseOfferDTO addClothesOffer(String name, String description, String url, Double price, String clothingCategory, String gender, String size, MultipartFile image) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty() || url == null || url.isEmpty() || price == null || price <= 0 || clothingCategory == null || clothingCategory.isEmpty() || image == null || image.isEmpty()) {
            return new ResponseOfferDTO("Invalid input data");
        }
        try {
            String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir).resolve(imageName).normalize();
            Files.createDirectories(imagePath.getParent());
            Files.copy(image.getInputStream(), imagePath);
            System.out.println(Gender.valueOf(gender.toUpperCase()));
            ClothesOffer offer = new ClothesOffer(name, description, url, price, ClothingCategory.valueOf(clothingCategory.toUpperCase()), Gender.valueOf(gender.toUpperCase()), size, imageName);

            Set<ConstraintViolation<ClothesOffer>> violations = validator.validate(offer);
            if (!violations.isEmpty()) {
                return new ResponseOfferDTO("Validation errors: " + violations);
            }

            clothesRepository.save(offer);

            return new ResponseOfferDTO(true, offer);
        } catch (IOException e) {
            return new ResponseOfferDTO("Failed to save offer");
        } catch (Exception e) {
            return new ResponseOfferDTO("An unexpected error occurred");
        }
    }


    public ResponseOfferDTO deleteClothesOffer(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid ID");
        }
        Optional<ClothesOffer> clothesOffer = clothesRepository.findById(id);
        if (clothesOffer.isPresent()) {
            ClothesOffer offer = clothesOffer.get();
            if (offer.getImageName() != null && !offer.getImageName().isEmpty()) {
                Path imagePath = Paths.get("uploads").resolve(offer.getImageName()).normalize();
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    return new ResponseOfferDTO("Failed to delete image from disk");
                }
            }
            clothesRepository.delete(offer);
            return new ResponseOfferDTO(true, id);
        }
        return new ResponseOfferDTO("Offer not found");
    }

    public ResponseOfferDTO updateClothesOffer(UUID id, ClothesOffer updatedOffer, MultipartFile image) {
        if (id == null || updatedOffer == null) {
            return new ResponseOfferDTO(false, null, "Invalid data");
        }
        Optional<ClothesOffer> existingOffer = clothesRepository.findById(id);
        if (existingOffer.isPresent()) {
            ClothesOffer offer = existingOffer.get();
            offer.setName(updatedOffer.getName());
            offer.setDescription(updatedOffer.getDescription());
            offer.setUrl(updatedOffer.getUrl());
            offer.setPrice(updatedOffer.getPrice());
            offer.setClothingCategory(updatedOffer.getClothingCategory());
            offer.setGender(updatedOffer.getGender());
            offer.setSize(updatedOffer.getSize());

            if (image != null && !image.isEmpty()) {
                try {
                    String imageName = id + "_" + image.getOriginalFilename();
                    Path imagePath = Paths.get(uploadDir).resolve(imageName).normalize();
                    Files.createDirectories(imagePath.getParent());
                    Files.copy(image.getInputStream(), imagePath);

                    offer.setImageName(imageName);
                } catch (IOException e) {
                    return new ResponseOfferDTO(false, null, "Failed to update image");
                }
            }

            Set<ConstraintViolation<ClothesOffer>> violations = validator.validate(offer);
            if (!violations.isEmpty()) {
                return new ResponseOfferDTO(false, null, "Validation errors: " + violations);
            }

            clothesRepository.save(offer);
            return new ResponseOfferDTO(true, offer);
        }
        return new ResponseOfferDTO(false, null, "Offer not found");
    }

    public ResponseOfferDTO findOffersByCategoryAndGender(ClothingCategory category, Gender gender) {
        List<ClothesOffer> offers;
        if (category == null) {
            if (gender != null) {
                offers = clothesRepository.findByGender(gender);
            } else {
                offers = clothesRepository.findAll();
            }
        } else {
            if (gender != null) {
                offers = clothesRepository.findByClothingCategoryAndGender(category, gender);
            } else {
                offers = clothesRepository.findByClothingCategory(category);
            }
        }
        return new ResponseOfferDTO(true, offers);
    }


    public void saveImage(UUID id, MultipartFile image) throws IOException {
        if (id == null || image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID or image");
        }
        Optional<ClothesOffer> offer = clothesRepository.findById(id);
        if (offer.isPresent()) {
            String imageName = id + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            ClothesOffer existingOffer = offer.get();
            existingOffer.setImageName(imageName);
            clothesRepository.save(existingOffer);
        } else {
            throw new IOException("Offer not found");
        }
    }

    public ResponseEntity<Resource> getImage(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Path imagePath = Paths.get(uploadDir).resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(imagePath))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
