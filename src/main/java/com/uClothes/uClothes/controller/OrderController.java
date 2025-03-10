package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.dto.OrderRequestDTO;
import com.uClothes.uClothes.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Value("${WEB_URL}")
    private String webUrl;
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> createOrderWithPayment(@RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            String successUrl = webUrl + "checkout/success";
            String cancelUrl = webUrl + "checkout/cancel";

            Map<String, String> response = orderService.createPaymentSession(orderRequestDTO, successUrl, cancelUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to process payment"));
        }
    }
}



