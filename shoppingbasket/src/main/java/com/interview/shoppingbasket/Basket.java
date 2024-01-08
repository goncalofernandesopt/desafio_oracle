package com.interview.shoppingbasket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Basket {
    private List<BasketItem> items = new ArrayList<>();

    public void add(String productCode, String productName, int quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode(productCode);
        basketItem.setProductName(productName);
        basketItem.setQuantity(quantity);

        items.add(basketItem);
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void consolidateItems() {

        HashMap<String, BasketItem> conjuntoNomeItem = new HashMap<>();

        for(BasketItem itemAtualIteracao : new ArrayList<>(items)){


            if(conjuntoNomeItem.containsKey(itemAtualIteracao.getProductCode())){
                BasketItem itemDaLista = conjuntoNomeItem.get(itemAtualIteracao.getProductCode());
                itemDaLista.setQuantity(itemDaLista.getQuantity() + itemAtualIteracao.getQuantity());

                conjuntoNomeItem.put(itemAtualIteracao.getProductCode(), itemDaLista);
                items.remove(itemAtualIteracao);

            }else {
                BasketItem itemParaConsolidar = new BasketItem();
                itemParaConsolidar.setProductCode(itemAtualIteracao.getProductCode());
                itemParaConsolidar.setProductName(itemAtualIteracao.getProductName());
                itemParaConsolidar.setQuantity(itemAtualIteracao.getQuantity());
                itemParaConsolidar.setProductRetailPrice(itemAtualIteracao.getProductRetailPrice());

                conjuntoNomeItem.put(itemAtualIteracao.getProductCode(), itemParaConsolidar);

                items.remove(itemAtualIteracao);

            }
        }

        items.addAll(conjuntoNomeItem.values());

    }
}
