package com.uClothes.uClothes.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.uClothes.uClothes.service.WebhookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Value("${STRIPE_WEBHOOK_SECRET}")
    private String stripeWebhookSecret;

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(
                    payload, sigHeader, stripeWebhookSecret
            );

            webhookService.handleStripeEvent(event);

            return ResponseEntity.ok("Event processed successfully");
        } catch (SignatureVerificationException e) {
            System.err.println("Error verifying webhook signature: " + e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("Error processing webhook event: " + e.getMessage());
            return ResponseEntity.status(500).body("Webhook processing error");
        }
    }

}

