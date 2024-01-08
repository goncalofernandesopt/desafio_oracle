package com.interview.shoppingbasket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class RetailPriceCheckoutStepTest {

    PricingService pricingService;
    CheckoutContext checkoutContext;
    Basket basket;

    PromotionsService promotionsService;

    BasketItem productPromoTest = new BasketItem();
    BasketItem productPromoTest2 = new BasketItem();
    BasketItem productPromoTest3 = new BasketItem();
    BasketItem productPromoTest4 = new BasketItem();

    Promotion promo = new Promotion();
    Promotion promo2 = new Promotion();
    Promotion promo3 = new Promotion();
    Promotion promo4 = new Promotion();

    List<Promotion> promotionList = new ArrayList<>();

    @BeforeEach
    void setup() {
        pricingService = Mockito.mock(PricingService.class);
        checkoutContext = Mockito.mock(CheckoutContext.class);
        promotionsService = Mockito.mock(PromotionsService.class);



        productPromoTest.setProductCode("productPromoTest1");
        productPromoTest.setProductName("productPromoTest2f1");

        productPromoTest2.setProductCode("productPromoTest2");
        productPromoTest2.setProductName("productPromoTest50%");

        productPromoTest3.setProductCode("productPromoTest3");
        productPromoTest3.setProductName("productPromoTest10%");

        productPromoTest4.setProductCode("productPromoTest4");
        productPromoTest4.setProductName("productPromoTestNoPromo");



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




        basket = new Basket();

        when(checkoutContext.getBasket()).thenReturn(basket);
    }

    @Test
    void setPriceZeroForEmptyBasket() {

        RetailPriceCheckoutStep retailPriceCheckoutStep = new RetailPriceCheckoutStep(pricingService);

        retailPriceCheckoutStep.execute(checkoutContext);

        Mockito.verify(checkoutContext).setRetailPriceTotal(0.0);
    }

    @Test
    void setPriceOfProductToBasketItem() {

        basket.add("product1", "myproduct1", 10);
        basket.add("product2", "myproduct2", 10);

        when(pricingService.getPrice("product1")).thenReturn(3.99);
        when(pricingService.getPrice("product2")).thenReturn(2.0);
        RetailPriceCheckoutStep retailPriceCheckoutStep = new RetailPriceCheckoutStep(pricingService);

        retailPriceCheckoutStep.execute(checkoutContext);
        Mockito.verify(checkoutContext).setRetailPriceTotal(5.99d);

    }

    @Test
    void applyPromotion2f1() {

        RetailPriceCheckoutStep retailPriceStep = new RetailPriceCheckoutStep(pricingService);
        productPromoTest.setProductRetailPrice(15);
        productPromoTest.setQuantity(10);

        double discountedPrice = retailPriceStep.applyPromotion(promo, productPromoTest, productPromoTest.getProductRetailPrice());

        assertEquals(75.0, discountedPrice, 0.001);

    }

    @Test
    void applyPromotion50off() {

        RetailPriceCheckoutStep retailPriceStep = new RetailPriceCheckoutStep(pricingService);
        productPromoTest2.setProductRetailPrice(15);
        productPromoTest2.setQuantity(10);

        double discountedPrice = retailPriceStep.applyPromotion(promo2, productPromoTest2, productPromoTest2.getProductRetailPrice());

        assertEquals(75.0, discountedPrice, 0.001);

    }

    @Test
    void applyPromotion10off() {

        RetailPriceCheckoutStep retailPriceStep = new RetailPriceCheckoutStep(pricingService);
        productPromoTest3.setProductRetailPrice(15);
        productPromoTest3.setQuantity(10);

        double discountedPrice = retailPriceStep.applyPromotion(promo3, productPromoTest3, productPromoTest3.getProductRetailPrice());

        assertEquals(135.0, discountedPrice, 0.001);

    }

    @Test
    void applyPromotionNoPromo() {

        RetailPriceCheckoutStep retailPriceStep = new RetailPriceCheckoutStep(pricingService);
        productPromoTest4.setProductRetailPrice(15);
        productPromoTest4.setQuantity(10);

        double discountedPrice = retailPriceStep.applyPromotion(promo4, productPromoTest4, productPromoTest4.getProductRetailPrice());

        assertEquals(150.0, discountedPrice, 0.001);

    }

    @Test
    void applyPromotions() {
        RetailPriceCheckoutStep retailPriceStep = new RetailPriceCheckoutStep(pricingService);
        productPromoTest4.setProductRetailPrice(15);
        productPromoTest4.setQuantity(10);

        double discountedPrice = retailPriceStep.applyPromotions(promotionList, productPromoTest4, productPromoTest4.getProductRetailPrice());

        assertEquals(150.0, discountedPrice, 0.001);
    }
}
