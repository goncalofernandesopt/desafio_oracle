package com.interview.shoppingbasket;

import java.util.ArrayList;
import java.util.List;

public class PromotionCheckoutStep implements CheckoutStep{

    private List<Promotion> promotionList = new ArrayList<>();
    PromotionsService promotionsService;

    public PromotionCheckoutStep(List<Promotion> promotionList, PromotionsService promotionsService) {
        this.promotionList = promotionList;
        this.promotionsService = promotionsService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        promotionList = promotionsService.getPromotions(basket);
        checkoutContext.setPromotionList(promotionList);
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }


    //Não tinha a certeza se era suposto criar um método para o programa "trazer" apenas as promoções aplicadas
    //Aos items do basket ou se era suposto assumir que o programa já faria isso automaticamente através da função
    //getPromotions.
    //Para todos os efeitos, assumi a última opção e fiz o mock de 4 promoções nos testes.
    //No entanto, está em baixo uma possível implementação desse método da interface para trazer de uma lista com todas
    //as promoções, apenas aquelas que estão associadas ao basket.

    /*
    @Override
    public List<Promotion> getPromotions(Basket basket) {
        List<Promotion> applicablePromotions = new ArrayList<>();

        for (BasketItem item : basket.getItems()) {
            String productCode = item.getProductCode();
            List<Promotion> matchingPromotions = findPromotionsForProductCode(productCode);

            applicablePromotions.addAll(matchingPromotions);
        }

        return applicablePromotions;
    }

    private List<Promotion> findPromotionsForProductCode(String productCode) {
        List<Promotion> matchingPromotions = new ArrayList<>();

        for (Promotion promotion : promotionList) {
            if (productCode.equals(promotion.getProductCode())) {
                matchingPromotions.add(promotion);
            }
        }

        return matchingPromotions;
    }
    */
}
