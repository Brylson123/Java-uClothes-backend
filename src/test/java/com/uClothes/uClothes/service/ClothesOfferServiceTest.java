package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.ClothesOffer;
import com.uClothes.uClothes.domain.ClothingCategory;
import com.uClothes.uClothes.domain.Gender;
import com.uClothes.uClothes.dto.ResponseOfferDTO;
import com.uClothes.uClothes.repositories.ClothesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClothesOfferServiceTest {

    @Mock
    private ClothesRepository clothesRepository;

    @InjectMocks
    private ClothesOfferService clothesOfferService;
    private AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testFindAllOffers() {
        when(clothesRepository.findAll()).thenReturn(Collections.emptyList());
        ResponseOfferDTO response = clothesOfferService.findAllOffers();
        assertTrue(response.isSuccess());
        assertTrue(response.getOffers().isEmpty());
    }

    @Test
    void testFindOfferSuccess() {
        UUID id = UUID.randomUUID();
        ClothesOffer offer = new ClothesOffer();
        when(clothesRepository.findById(id)).thenReturn(Optional.of(offer));
        ResponseOfferDTO response = clothesOfferService.findOffer(id);
        assertTrue(response.isSuccess());
        assertNotNull(response.getOffer());
    }

    @Test
    void testFindOfferNotFound() {
        UUID id = UUID.randomUUID();
        when(clothesRepository.findById(id)).thenReturn(Optional.empty());
        ResponseOfferDTO response = clothesOfferService.findOffer(id);
        assertFalse(response.isSuccess());
        assertNull(response.getOffer());
        assertEquals("Offer not found", response.getError());
    }

    @Test
    void testAddClothesOfferSuccess() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        when(image.getOriginalFilename()).thenReturn("test.jpg");

        Path testImagePath = Paths.get("src/test/resources/test.jpg");
        Files.createDirectories(testImagePath.getParent());
        if (!Files.exists(testImagePath)) {
            Files.createFile(testImagePath);
        }

        when(image.getInputStream()).thenReturn(Files.newInputStream(testImagePath));
        when(image.isEmpty()).thenReturn(false);
        when(clothesRepository.save(any(ClothesOffer.class))).thenReturn(new ClothesOffer());

        ResponseOfferDTO response = clothesOfferService.addClothesOffer("Name", "Description", "http://url.com", 100.0, "SHIRTS", "MAN", "M", image);
        assertTrue(response.isSuccess());
        assertNotNull(response.getOffer());
    }

    @Test
    void testAddClothesOfferInvalidData() {
        ResponseOfferDTO response = clothesOfferService.addClothesOffer(null, "Description", "http://url.com", 100.0, "SHIRTS", "MAN", "M", null);
        assertFalse(response.isSuccess());
        assertEquals("Invalid input data", response.getError());
    }

    @Test
    void testDeleteClothesOfferSuccess() throws IOException {
        UUID id = UUID.randomUUID();
        ClothesOffer offer = new ClothesOffer();
        offer.setImageName("test.jpg");
        when(clothesRepository.findById(id)).thenReturn(Optional.of(offer));
        doNothing().when(clothesRepository).delete(any(ClothesOffer.class));

        Path testImagePath = Paths.get("uploads/test.jpg");
        Files.createDirectories(testImagePath.getParent());
        if (Files.exists(testImagePath)) {
            Files.delete(testImagePath);
        }
        Files.createFile(testImagePath);

        ResponseOfferDTO response = clothesOfferService.deleteClothesOffer(id);
        assertTrue(response.isSuccess());
    }

    @Test
    void testDeleteClothesOfferNotFound() {
        UUID id = UUID.randomUUID();
        when(clothesRepository.findById(id)).thenReturn(Optional.empty());

        ResponseOfferDTO response = clothesOfferService.deleteClothesOffer(id);
        assertFalse(response.isSuccess());
        assertEquals("Offer not found", response.getError());
    }

    @Test
    void testUpdateClothesOfferSuccess() throws IOException {
        UUID id = UUID.randomUUID();
        ClothesOffer offer = new ClothesOffer();
        ClothesOffer updatedOffer = new ClothesOffer();
        MultipartFile image = mock(MultipartFile.class);
        when(clothesRepository.findById(id)).thenReturn(Optional.of(offer));
        when(image.getOriginalFilename()).thenReturn("test.jpg");

        Path testImagePath = Paths.get("src/test/resources/test.jpg");
        Files.createDirectories(testImagePath.getParent());
        if (!Files.exists(testImagePath)) {
            Files.createFile(testImagePath);
        }

        when(image.getInputStream()).thenReturn(Files.newInputStream(testImagePath));
        when(image.isEmpty()).thenReturn(false);
        when(clothesRepository.save(any(ClothesOffer.class))).thenReturn(updatedOffer);

        ResponseOfferDTO response = clothesOfferService.updateClothesOffer(id, updatedOffer, image);
        assertTrue(response.isSuccess());
    }

    @Test
    void testFindOffersByCategoryAndGender() {
        when(clothesRepository.findByClothingCategoryAndGender(ClothingCategory.SHIRTS, Gender.MAN)).thenReturn(Collections.emptyList());
        ResponseOfferDTO response = clothesOfferService.findOffersByCategoryAndGender(ClothingCategory.SHIRTS, Gender.MAN);
        assertTrue(response.isSuccess());
        assertTrue(response.getOffers().isEmpty());
    }

    @Test
    void testGetImageSuccess() throws IOException {
        String imageName = "test.jpg";
        Path testImagePath = Paths.get("uploads/test.jpg");
        Files.createDirectories(testImagePath.getParent());
        if (!Files.exists(testImagePath)) {
            Files.createFile(testImagePath);
        }

        ResponseEntity<Resource> response = clothesOfferService.getImage(imageName);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testGetImageNotFound() {
        String imageName = "not_exist.jpg";
        ResponseEntity<Resource> response = clothesOfferService.getImage(imageName);
        assertEquals(404, response.getStatusCode().value());
    }
}
