package com.uClothes.uClothes.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.repositories.ClothesOfferRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClothesOfferService {
    private final ClothesOfferRepository clothesRepository;

    private final Validator validator;

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    public ClothesOfferService(ClothesOfferRepository clothesRepository) {
        this.clothesRepository = clothesRepository;
        this.storage = StorageOptions.getDefaultInstance().getService();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        try {
            this.validator = factory.getValidator();
            factory.close();
        } catch (Throwable throwable) {
            if (factory != null)
                try {
                    factory.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
            throw throwable;
        }
    }

    public ResponseOfferDTO findAllOffers() {
        List<ClothesOffer> offers = this.clothesRepository.findAll();
        return new ResponseOfferDTO(true, offers);
    }
    public ResponseOfferDTO findAllActiveOffers() {
        List<ClothesOffer> offers = this.clothesRepository.findByActiveTrue();
        return new ResponseOfferDTO(true, offers);
    }

    public ResponseOfferDTO findOffer(UUID id) {
        if (id == null)
            return new ResponseOfferDTO(false, null, "Invalid ID");
        Optional<ClothesOffer> offer = this.clothesRepository.findById(id);
        return offer.map(clothesOffer -> new ResponseOfferDTO(true, clothesOffer))
                .orElseGet(() -> new ResponseOfferDTO(false, id, "Offer not found"));
    }
    public ResponseOfferDTO findActiveOffer(UUID id) {
        if (id == null) {
            return new ResponseOfferDTO(false, null, "Invalid ID");
        }
        Optional<ClothesOffer> offer = clothesRepository.findById(id)
                .filter(ClothesOffer::getActive);

        return offer.map(clothesOffer -> new ResponseOfferDTO(true, clothesOffer))
                .orElseGet(() -> new ResponseOfferDTO(false, id, "Active offer not found"));
    }

    public ResponseOfferDTO addClothesOffer(String name, String description, Double price, String clothingCategory, String gender, String size, Boolean active, MultipartFile image) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty() || price == null || price <= 0.0D || clothingCategory == null || clothingCategory.isEmpty() || image == null || image.isEmpty())
            return new ResponseOfferDTO("Invalid input data");
        try {
            String imageName =UUID.randomUUID() + "_" + UUID.randomUUID();
            uploadToGCS(imageName, image);
            ClothesOffer offer = new ClothesOffer(name, description, price, ClothingCategory.valueOf(clothingCategory.toUpperCase()), Gender.valueOf(gender.toUpperCase()), size, imageName, active);
            Set<ConstraintViolation<ClothesOffer>> violations = this.validator.validate(offer);
            if (!violations.isEmpty())
                return new ResponseOfferDTO("Validation errors: " + violations);
            this.clothesRepository.save(offer);
            return new ResponseOfferDTO(true, offer);
        } catch (IOException e) {
            return new ResponseOfferDTO("Failed to save offer");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseOfferDTO("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void uploadToGCS(String imageName, MultipartFile image) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder(this.bucketName, imageName).setContentType(image.getContentType()).build();
        this.storage.create(blobInfo, image.getBytes());
    }

    public ResponseOfferDTO deleteClothesOffer(UUID id) {
        if (id == null)
            throw new IllegalArgumentException("Invalid ID");
        Optional<ClothesOffer> clothesOffer = this.clothesRepository.findById(id);
        if (clothesOffer.isPresent()) {
            ClothesOffer offer = clothesOffer.get();
            if (offer.getImageName() != null && !offer.getImageName().isEmpty())
                this.storage.delete(this.bucketName, offer.getImageName());
            this.clothesRepository.delete(offer);
            return new ResponseOfferDTO(true, id);
        }
        return new ResponseOfferDTO("Offer not found");
    }

    public ResponseOfferDTO updateClothesOffer(UUID id, ClothesOffer updatedOffer, MultipartFile image) {
        if (id == null || updatedOffer == null)
            return new ResponseOfferDTO(false, null, "Invalid data");
        Optional<ClothesOffer> existingOffer = this.clothesRepository.findById(id);
        if (existingOffer.isPresent()) {
            ClothesOffer offer = existingOffer.get();
            offer.setName(updatedOffer.getName());
            offer.setDescription(updatedOffer.getDescription());
            offer.setPrice(updatedOffer.getPrice());
            offer.setActive(updatedOffer.getActive());
            offer.setClothingCategory(updatedOffer.getClothingCategory());
            offer.setGender(updatedOffer.getGender());
            offer.setSize(updatedOffer.getSize());
            if (image != null && !image.isEmpty())
                try {
                    String imageName = id + "_" + id;
                    uploadToGCS(imageName, image);
                    offer.setImageName(imageName);
                } catch (IOException e) {
                    return new ResponseOfferDTO(false, null, "Failed to update image");
                }
            Set<ConstraintViolation<ClothesOffer>> violations = this.validator.validate(offer);
            if (!violations.isEmpty())
                return new ResponseOfferDTO(false, null, "Validation errors: " + violations);
            this.clothesRepository.save(offer);
            return new ResponseOfferDTO(true, offer);
        }
        return new ResponseOfferDTO(false, null, "Offer not found");
    }

    public ResponseOfferDTO findOffersByCategoryAndGender(ClothingCategory category, Gender gender) {
        List<ClothesOffer> offers;
        if (category == null) {
            if (gender != null) {
                offers = this.clothesRepository.findByGenderAndActiveTrue(gender);
            } else {
                offers = this.clothesRepository.findByActiveTrue();
            }
        } else if (gender != null) {
            offers = this.clothesRepository.findByClothingCategoryAndGenderAndActiveTrue(category, gender);
        } else {
            offers = this.clothesRepository.findByClothingCategoryAndActiveTrue(category);
        }
        return new ResponseOfferDTO(true, offers);
    }

    public ResponseEntity<Resource> getImage(String imageName) {
        if (imageName == null || imageName.isEmpty())
            return ResponseEntity.badRequest().build();
        try {
            String publicUrl = String.format("https://storage.googleapis.com/%s/%s",   this.bucketName, imageName );
            UrlResource urlResource = new UrlResource(URI.create(publicUrl));
            return (ResponseEntity.ok()
                    .header("Content-Disposition", new String[] { "inline; filename=\"" + imageName + "\"" })).body(urlResource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
