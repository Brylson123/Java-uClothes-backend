package com.uClothes.uClothes.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    @Value("${STRIPE_API_SECRET}")
    private String stripeSecretKey;

    public PaymentService() {
    }

    public String createCheckoutSession(String productName, long amount, String currency, String successUrl,
                                        String cancelUrl, String customerEmail, String productImageUrl, UUID productId,
                                        Map<String, String> metadata) throws Exception {
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(String.valueOf(customerEmail))
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setClientReferenceId(productId.toString())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(productName)
                                        .addImage(productImageUrl)
                                        .build())
                                .setUnitAmount(amount)
                                .build())
                        .setQuantity(1L)
                        .build());

        if (metadata != null) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                paramsBuilder.putMetadata(entry.getKey(), entry.getValue());
            }
        }

        SessionCreateParams params = paramsBuilder.build();

        Session session = Session.create(params);

        return session.getUrl();
    }


}
