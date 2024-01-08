package com.interview.shoppingbasket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckoutPipelineTest {

    CheckoutPipeline checkoutPipeline;

    PricingService pricingService;

    PromotionsService promotionsService;

    @Mock
    Basket basket;

    @Mock
    CheckoutStep checkoutStep2;

    PromotionCheckoutStep promotionStep;

    Promotion promo = new Promotion();
    Promotion promo2 = new Promotion();
    Promotion promo3 = new Promotion();
    Promotion promo4 = new Promotion();

    List<Promotion> promotionList = new ArrayList<>();

    @BeforeEach
    void setup() {
        checkoutPipeline = new CheckoutPipeline();
        pricingService = Mockito.mock(PricingService.class);
        promotionsService = Mockito.mock(PromotionsService.class);


        checkoutStep2 = new RetailPriceCheckoutStep(pricingService);

        promo.setProductCode("productPromoTest1");
        promo.setPromotionType("2f1");

        promo2.setProductCode("productPromoTest2");
        promo2.setPromotionType("50%");

        promo3.setProductCode("productPromoTest3");
        promo3.setPromotionType("10%");

        promo4.setProductCode("productPromoTest4");
        promo4.setPromotionType("");

        promotionList.add(promo);
        promotionList.add(promo2);
        promotionList.add(promo3);
        promotionList.add(promo4);

        //promotionStep = new PromotionCheckoutStep(promotionList);

        promotionStep = new PromotionCheckoutStep(promotionList,promotionsService);

        basket = new Basket();

    }

    @Test
    void returnZeroPaymentForEmptyPipeline() {
        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        assertEquals(paymentSummary.getRetailTotal(), 0.0);
    }

    @Test
    void executeAllPassedCheckoutSteps() {
        promotionStep.setPromotionList(promotionList);

        when(promotionsService.getPromotions(any())).thenReturn(promotionList);

        checkoutPipeline.addStep(promotionStep);
        checkoutPipeline.addStep(checkoutStep2);

        basket.add("productPromoTest1", "myproduct1", 10);
        basket.add("productPromoTest2", "myproduct2", 10);

        when(pricingService.getPrice("productPromoTest1")).thenReturn(5.00);
        when(pricingService.getPrice("productPromoTest2")).thenReturn(2.0);


        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        assertEquals(paymentSummary.getRetailTotal(), 35.0);
    }

}
