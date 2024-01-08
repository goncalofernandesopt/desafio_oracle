package com.interview.shoppingbasket;

import java.util.List;

public class RetailPriceCheckoutStep implements CheckoutStep {
    private PricingService pricingService;
    private double retailTotal;

    public RetailPriceCheckoutStep(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        List<Promotion> allPromotions = checkoutContext.getPromotionList();
        retailTotal = 0.0;

        for (BasketItem basketItem: basket.getItems()) {
            double price = pricingService.getPrice(basketItem.getProductCode());
            basketItem.setProductRetailPrice(price);

            retailTotal += applyPromotions(allPromotions, basketItem, basketItem.getProductRetailPrice());
        }

        checkoutContext.setRetailPriceTotal(retailTotal);
    }

    public double applyPromotion(Promotion promotion, BasketItem item, double price) {
        switch (promotion.getPromotionType()) {
            case "2f1":
                int quantity = item.getQuantity();
                int discountFreeItems = quantity / 2;
                int payableItems = quantity - discountFreeItems;

                return payableItems * price;
            case "50%":
                return price * item.getQuantity() * 0.5;
            case "10%":
                return price * item.getQuantity() * 0.9;
        }

        return price * item.getQuantity();
    }

    public double applyPromotions(List<Promotion> promotionsList, BasketItem item, double price) {
        for(Promotion promo : promotionsList){
            if(promo.getProductCode().equals(item.getProductCode())){
                price = applyPromotion(promo, item, price);
            }
        }

        return price;
    }
}
